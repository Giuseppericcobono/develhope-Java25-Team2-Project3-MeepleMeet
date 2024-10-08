package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.EventDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.EventStatusEnum;
import co.develhope.team2.meeplemeet_project_team2.repositories.EventRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.PlaceRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private EventRepository eventRepository;

    // this method return a list of partecipants at the event
    private List<String> getPartecipants(Event e) {
        return e.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    //this method transform an object event in an object eventDTO
    public EventDTO createEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setCreator(event.getUser().getUsername());
        eventDTO.setName(event.getName());
        eventDTO.setNameGame(event.getNameGame());
        eventDTO.setDateTimeEvent(event.getDateTimeEvent());
        eventDTO.setMaxCapacityEvent(event.getMaxCapacityEvent());
        eventDTO.setAddress(event.getPlace().getAddress());
        eventDTO.setPlaceName(event.getPlace().getName());
        eventDTO.setPartecipants(getPartecipants(event));
        eventDTO.setEventStatusEnum(event.getEventStatusEnum());
        return eventDTO;
    }

    // this method transform an event list in an eventDTO list
    public List<EventDTO> createListEventDTO(List<Event> event) {
        List<EventDTO> eventDTOList = new ArrayList<>();
        for(Event e : event) {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setCreator(e.getUser().getUsername());
            eventDTO.setName(e.getName());
            eventDTO.setNameGame(e.getNameGame());
            eventDTO.setDateTimeEvent(e.getDateTimeEvent());
            eventDTO.setMaxCapacityEvent(e.getMaxCapacityEvent());
            eventDTO.setAddress(e.getPlace().getAddress());
            eventDTO.setPlaceName(e.getPlace().getName());
            eventDTO.setPartecipants(getPartecipants(e));
            eventDTO.setEventStatusEnum(e.getEventStatusEnum());
            eventDTOList.add(eventDTO);
        }
        return eventDTOList;
    }

    @Transactional
    public Optional<Event> createEvent(Integer userId, Integer placeId, Event event) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Place> optionalPlace = placeRepository.findById(placeId);

        if(optionalUser.isPresent() && optionalPlace.isPresent()){

            Place place = optionalPlace.get();
            User user = optionalUser.get();
            /*
            controllo se la capacita dell'evento e inferiore o uguale a quella di plase,
            se e vero crea l'evento e sottrale la capacita massima di place
             */

            if(event.getMaxCapacityEvent() <= place.getMaxCapacity()){
                event.setUser(user);
                event.setPlace(place);

                Event saveEvent = eventRepository.save(event);
                place.setMaxCapacity(place.getMaxCapacity() - event.getMaxCapacityEvent());
                usersEnrolled(user.getUserId(), event.getId());
                placeRepository.save(place);
                return Optional.of(saveEvent);
            }else {
                return Optional.empty();
            }

        }else {
            return  Optional.empty();
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 10000)
    public void updateAutoEventStatus() {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        List<Event> notStartedEvent = eventRepository.findEventsByStatus(EventStatusEnum.NOT_STARTED);

        logger.info("Number of events to be updated: {}", notStartedEvent.size());

        Event saveEvent = null;
        for (Event event : notStartedEvent) {

            if (event.getEventStatusEnum() == EventStatusEnum.NOT_STARTED && event.getDateTimeEvent().isBefore(localDateTimeNow) || event.getDateTimeEvent().isEqual(localDateTimeNow)) {
                logger.info("Status update for event with ID: {}", event.getId());
                event.setEventStatusEnum(EventStatusEnum.IN_PROGRESS);
                eventRepository.save(event);
                logger.info("Updated status for the event with ID: {}", event.getId());
            }
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 3600000)// execution every hour
    public void updateFinishEventAuto() {

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        List<Event> startedEvent = eventRepository.findEventsByStatus(EventStatusEnum.IN_PROGRESS);

        for (Event event : startedEvent) {
            LocalDateTime eventEndTime = event.getDateTimeEvent().plusHours(12);

            if (localDateTimeNow.isAfter(eventEndTime)) {

                event.setEventStatusEnum(EventStatusEnum.FINISHED);
                eventRepository.save(event);

                addMaxCapacity(event.getId());
            }
        }
    }

    public Optional<List<EventDTO>> getAllEvent() {
        List<Event> allEvent = eventRepository.findAllNotDeleted();
        if(allEvent.isEmpty()) {
            return Optional.empty();
        }
        List<EventDTO> allEventDTO = createListEventDTO(allEvent);
         return Optional.of(allEventDTO);
    }
    public Optional<EventDTO> getEventById(Integer id) {
        Event eventById = eventRepository.findByIdAndNotDeleted(id);
        EventDTO eventDTO = createEventDTO(eventById);
        return Optional.of(eventDTO);
    }
    public Optional<List<EventDTO>> getAllEventDeleted() {
        List<Event> allEventDeleted = eventRepository.findAllDeleted();
        if(allEventDeleted.isEmpty()) {
            return Optional.empty();
        }
        List<EventDTO> allEventDTODeleted = createListEventDTO(allEventDeleted);
        return Optional.of(allEventDTODeleted);
    }
    public Optional<EventDTO> getEventByIdDeleted(Integer id) {
        Event eventById = eventRepository.findByIdAndDeleted(id);
        EventDTO eventDTO = createEventDTO(eventById);
        return Optional.of(eventDTO);
    }

    public Optional<Event> updateEvent(Integer id, Event updatedEvent) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event exsistEvent = eventOptional.get();

            // Update fields of existingEvent with values from updatedEvent
            exsistEvent.setName(updatedEvent.getName());
            exsistEvent.setNameGame(updatedEvent.getNameGame());
            exsistEvent.setDescriptionGame(updatedEvent.getDescriptionGame());
            exsistEvent.setDateTimeEvent(updatedEvent.getDateTimeEvent());
            exsistEvent.setMaxCapacityEvent(updatedEvent.getMaxCapacityEvent());

            // Save the updated event
            Event saveEvent = eventRepository.save(exsistEvent);
            return Optional.of(saveEvent);
        } else {
           return Optional.empty();
        }
    }

    public Optional<Event>deleteEventById(Integer id) {
        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            eventRepository.deleteById(id);
            return Optional.of(event);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Event> terminatedEvent(Integer id){
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(eventOptional.isPresent()){
            Event event = eventOptional.get();

            event.setEventStatusEnum(EventStatusEnum.FINISHED);
            addMaxCapacity(id);
            eventRepository.save(event);

            return Optional.of(event);

        }else {
          return Optional.empty();
        }
    }

    public Optional<Event> logicDeletion(Integer id){
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(eventOptional.isPresent()){
            Event event = eventOptional.get();
            event.setDeleted(true);
            Place place = event.getPlace();
            place.setMaxCapacity(place.getMaxCapacity() + event.getMaxCapacityEvent());
            placeRepository.save(place);
            eventRepository.save(event);
            return eventOptional;
        }
        return Optional.empty();
    }

    public Optional<Event> usersEnrolled(Integer userId, Integer eventId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        // check if the event and the user exist
        if (eventOptional.isPresent() && userOptional.isPresent()) {
            User user = userOptional.get();
            Event event = eventOptional.get();
            // check if there is space to register
            if(event.getMaxCapacityEvent() > 0) {
                // check if the user is already registered
                if(event.getUsers().contains(user)) {
                    logger.info("The user is already registered");
                    return Optional.empty();
                }
                event.getUsers().add(user);
                user.getEvent().add(event);

                event.setMaxCapacityEvent(event.getMaxCapacityEvent() - 1);
                eventRepository.save(event);
                userRepository.save(user);

                return Optional.of(event);
            } else {
                logger.info("Event max capacity is over");
                return Optional.empty();
            }
        } else {
            logger.info("User or Event not found");
            return Optional.empty();
        }
    }

    public Optional<Event> userUnsubscribe(Integer userId, Integer eventId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        // check if the event and the user exist
        if(userOptional.isPresent() && eventOptional.isPresent()) {
            User user = userOptional.get();
            Event event = eventOptional.get();
            // initialize a new list
            if (event.getUsers() == null) {
                event.setUsers(new ArrayList<>());
            }
            // check if the user is subscribed to the event
            if(user.getEvent().contains(event) && event.getUsers().contains(user)) {
                event.getUsers().remove(user);
                user.getEvent().remove(event);

                event.setMaxCapacityEvent(event.getMaxCapacityEvent() + 1);
                eventRepository.save(event);
                userRepository.save(user);

                return Optional.of(event);
            } else {
                logger.info("User with id: " + userId + " is not registered");
                return Optional.empty();
            }
        } else {
            logger.info("User or Event not found");
            return Optional.empty();
        }
    }

    public Optional<List<User>> listOfUserParticipateEvent(Integer eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return Optional.of(new ArrayList<>(event.getUsers()));
        } else {
            return Optional.empty();
        }
    }

    public void addMaxCapacity(Integer eventId){
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        Event event = eventOptional.get();
        Place place = event.getPlace();
        place.setMaxCapacity(place.getMaxCapacity() + event.getMaxCapacityEvent());
        placeRepository.save(place);
    }
    public Optional<List<EventDTO>> findEventsByEventNameContaining(String name) {
        List<Event> events = eventRepository.findEventsByEventNameContaining(name);
        List<EventDTO> eventDTOList = createListEventDTO(events);
        if (events.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(eventDTOList);
        }
    }

    public Optional<List<EventDTO>> findEventsByGameNameContaining(String gameName) {
        List<Event> events = eventRepository.findEventsByGameNameContaining(gameName);
        List<EventDTO> eventDTOList = createListEventDTO(events);
        if (events.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(eventDTOList);
        }
    }
}
