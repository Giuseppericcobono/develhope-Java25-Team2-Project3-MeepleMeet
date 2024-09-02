package co.develhope.team2.meeplemeet_project_team2.services;

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
import java.util.List;
import java.util.Optional;



@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private EventRepository eventRepository;



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

    public Optional<List<Event>> getAllEvent() {
        List<Event> allEvent = eventRepository.findAllNotDeleted();
         return Optional.of(allEvent);
    }
    public Optional<List<Event>> getAllEventDeleted() {
        List<Event> allEventDeleted = eventRepository.findAllDeleted();
        return Optional.of(allEventDeleted);
    }

    public Optional<Event> getEventById(Integer id) {
          return eventRepository.findById(id);
    }

    public Optional<Event> updateEvent(Integer id, Event updatedEvent) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventRepository.save(updatedEvent);
            return Optional.of(event);
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

    public void usersEnrolled(Integer userId, Integer eventId) {
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
                }
                event.getUsers().add(user);
                user.getEvent().add(event);

                event.setMaxCapacityEvent(event.getMaxCapacityEvent() - 1);
                eventRepository.save(event);
                userRepository.save(user);
            } else {
                logger.info("Event max capacity is over");
            }
        } else {
            logger.info("User or Event not found");
        }
    }

    public void userUnsubscribe(Integer userId, Integer eventId) {
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
            } else {
                logger.info("User with id: " + userId + " is not registered");
            }
        } else {
            logger.info("User or Event not found");
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

}
