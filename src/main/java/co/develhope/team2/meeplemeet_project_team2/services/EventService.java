package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.EventDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.repositories.EventRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public EventDTO createEvent(Integer userID, EventDTO eventDTO) {
        Optional<User> optionalUser = userRepository.findById(userID);
        if(optionalUser.isPresent()){

            User user = optionalUser.get();

            Event event = new Event();

            event.setName(eventDTO.getName());
            event.setNameGame(eventDTO.getNameGame());
            event.setDescriptionGame(eventDTO.getDescriptionGame());
            event.setDateTimeEvent(eventDTO.getDateTimeEvent());
            event.setMaxCapacity(eventDTO.getMaxCapacity());
            event.setLocation(eventDTO.getLocation());
            event.setEventStatusEnum(eventDTO.getEventStatusEnum());
            event.setUser(optionalUser.orElse(null));

            Event eventSave = eventRepository.save(event);

            return new EventDTO(
                    eventSave.getId(),
                    eventSave.getUser().getId(),
                    eventSave.getName(),
                    eventSave.getNameGame(),
                    eventSave.getDescriptionGame(),
                    eventSave.getDateTimeEvent(),
                    eventSave.getMaxCapacity(),
                    eventSave.getLocation(),
                    eventSave.getEventStatusEnum()
            );
        }else {
            throw new RuntimeException("User not found");
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
            // Handle the case where the book with the given id is not found
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }

        return updatedEvent;
    }

    public Event deleteEventById(Integer id) {
        eventRepository.deleteById(id);
        return null;
    }

}
