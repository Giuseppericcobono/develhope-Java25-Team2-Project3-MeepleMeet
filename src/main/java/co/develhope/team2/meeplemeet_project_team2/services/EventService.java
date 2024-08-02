package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.EventStatusEnum;
import co.develhope.team2.meeplemeet_project_team2.repositories.EventRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private EventRepository eventRepository;

    @Transactional
    public Event createEvent(Integer userId, Event event) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            event.setUser(optionalUser.get());
            event.setEventStatusEnum(EventStatusEnum.NOT_STARTED);
            return eventRepository.save(event);

        }else {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

    }
    @Transactional
    @Scheduled(fixedDelay = 10000)
    public Event updateAutoEventStatus(){
        Event saveEvent = null;
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        List<Event> allEvent = eventRepository.findAll();
        List<Event> notStartedEvent = allEvent.stream().filter(event -> event.getEventStatusEnum() == EventStatusEnum.NOT_STARTED).toList();
        logger.info("Numero di eventi da aggiornare: {}", notStartedEvent.size());

        for (Event event : notStartedEvent){

            if(event.getEventStatusEnum() == EventStatusEnum.NOT_STARTED && event.getDateTimeEvent().isBefore(localDateTimeNow) || event.getDateTimeEvent().isEqual(localDateTimeNow)){
                logger.info("Aggiornamento stato per l'evento con ID: {}", event.getId());
                event.setEventStatusEnum(EventStatusEnum.IN_PROGRESS);
                saveEvent = eventRepository.save(event);
                logger.info("Stato aggiornato per l'evento con ID: {}", event.getId());

            }
        
        }
        return saveEvent;
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
            // Handle the case where the book with the given id is not found
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
            // Handle the case where the book with the given id is not found
            throw new EntityNotFoundException("Event with id " + id + " not found");
        }

    }
}
