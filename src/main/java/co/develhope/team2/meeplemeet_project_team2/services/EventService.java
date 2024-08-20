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
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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
    public Event createEvent(Integer userId, Integer placeId, Event event) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Place> optionalPlace = placeRepository.findById(placeId);
        if(optionalUser.isPresent() && optionalPlace.isPresent()){
            event.setUser(optionalUser.get());
            event.setPlace(optionalPlace.get());
            event.setEventStatusEnum(EventStatusEnum.NOT_STARTED);
            return eventRepository.save(event);

        }else {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

    }
    @Transactional
    @Scheduled(fixedDelay = 10000)
    public void updateAutoEventStatus(){
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        List<Event> notStartedEvent = eventRepository.findEventsByStatus(EventStatusEnum.NOT_STARTED);
        logger.info("Numero di eventi da aggiornare: {}", notStartedEvent.size());
        for (Event event : notStartedEvent){
            if(localDateTimeNow.isEqual(event.getDateTimeEvent()) || localDateTimeNow.isAfter(event.getDateTimeEvent())){
                logger.info("Aggiornamento stato per l'evento con ID: {}", event.getId());
                event.setEventStatusEnum(EventStatusEnum.IN_PROGRESS);
                eventRepository.save(event);
                logger.info("Stato aggiornato per l'evento con ID: {}", event.getId());
            }
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 10000)//agg. ogni ora 3600000
    public void updateFinishEventAuto(){
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        List<Event> startedEvent = eventRepository.findEventsByStatus(EventStatusEnum.IN_PROGRESS);

        for (Event event : startedEvent){
            LocalDateTime eventEndeTime = event.getDateTimeEvent().plusMinutes(5);
            if(localDateTimeNow.isAfter(eventEndeTime) || localDateTimeNow.isEqual(eventEndeTime)){
                event.setEventStatusEnum(EventStatusEnum.FINISHED);
                eventRepository.save(event);
            }
        }
    }

    public List<Event> getAllEvent() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Integer id) {
        return eventRepository.findById(id);
    }

    public Event updateEvent(Integer id, Event updatedEvent) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(eventOptional.isPresent()){
            eventRepository.save(updatedEvent);
        }else {
            // Handle the case where the event with the given id is not found
            throw new EntityNotFoundException("Event with id " + id + " not found");
        }

        return updatedEvent;
    }

    public Event deleteEventById(Integer id) {
        eventRepository.deleteById(id);
        return null;
    }

    public Event terminatedEvent(Integer id){
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(eventOptional.isPresent()){
            Event event = eventOptional.get();
            event.setEventStatusEnum(EventStatusEnum.FINISHED);
            return eventRepository.save(event);
        }else {
            // Handle the case where the event with the given id is not found
            throw new EntityNotFoundException("Event with id " + id + " not found");
        }
    }

    //todo: rivedere per iscrizione di user ad eventi
    public Event usersEnrolled(Integer userId, Integer eventId){
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if(eventOptional.isPresent() && userOptional.isPresent()){
            User user = userOptional.get();
            Event event = eventOptional.get();
            List<User> users = event.getUsers();
            users.add(user);
            return eventRepository.save(event);
        } else {
            return null;
        }
    }
}
