package co.develhope.team2.meeplemeet_project_team2.controllers;


import co.develhope.team2.meeplemeet_project_team2.DTO.EventDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.services.EventService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Optional<Event>> create(@RequestParam(name = "user") Integer userId, @RequestParam(name = "place") Integer placeId, @RequestBody Event event) {
        Optional<Event> createEvent = eventService.createEvent(userId, placeId, event);
        return ResponseEntity.ok(createEvent);
    }

    @GetMapping("/list")
    public ResponseEntity<Optional<List<EventDTO>>> getList() {
        Optional<List<EventDTO>> eventList = eventService.getAllEvent();
        if (eventList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventList);
    }

    @GetMapping("/deleted/list")
    public ResponseEntity<Optional<List<EventDTO>>> getListDeleted() {
        Optional<List<EventDTO>> eventList = eventService.getAllEventDeleted();
        if (eventList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventList);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Optional<EventDTO>> getWithId(@PathVariable Integer id) {
        Optional<EventDTO> event = eventService.getEventById(id);
        if (event.isPresent()) {
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/search/deleted/{id}")
    public ResponseEntity<Optional<EventDTO>> getWithIdDeleted(@PathVariable Integer id) {
        Optional<EventDTO> event = eventService.getEventByIdDeleted(id);
        if (event.isPresent()) {
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<Event>> update(@PathVariable Integer id, @RequestBody @NotNull Event event) {
        Optional<Event> updateEvent = eventService.updateEvent(id, event);
        if (updateEvent.isPresent()) {
            return ResponseEntity.ok(updateEvent);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Optional<Event>> delete(@PathVariable Integer id) {
        Optional<Event> deletedEvent = eventService.deleteEventById(id);
        if (deletedEvent.isPresent()) {
            return ResponseEntity.ok(deletedEvent);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/logic/{id}")
    public ResponseEntity<Event> logicDeletion(@PathVariable Integer id) {
        Optional<EventDTO> eventId = eventService.getEventById(id);
        if (eventId.isPresent()) {
            Optional<Event> deletedEvent = eventService.logicDeletion(id);
            return ResponseEntity.ok(deletedEvent.get());
        }
        return ResponseEntity.badRequest().build();
    }

    //setStatusEvent a FINISHED
    @PutMapping("/setStatus/{id}")
    public ResponseEntity<Optional<Event>> setStatusEvent(@PathVariable Integer id, @NotNull Event event) {
        Optional<Event> updateEvent = eventService.terminatedEvent(id);
        if(updateEvent.isPresent()) {
        return ResponseEntity.ok(updateEvent);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/enrollments")
    public ResponseEntity<Event> eventsUsers(@RequestParam(name = "userID") Integer userId, @RequestParam(name = "eventID") Integer eventId) {
        Optional<Event> newEnroll = eventService.usersEnrolled(userId, eventId);
        if(newEnroll.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unsubscribe")
    public ResponseEntity<Event> unsuscribeUser(@RequestParam(name = "userID") Integer userId, @RequestParam(name = "eventID") Integer eventId) {
        Optional<Event> unsubscribe = eventService.userUnsubscribe(userId, eventId);
        if(unsubscribe.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/enrollments/user")
    public ResponseEntity<Optional<List<User>>> listOfPartecipants(@RequestParam(name = "eventID") Integer eventId) {
        Optional<List<User>> user = eventService.listOfUserParticipateEvent(eventId);
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search/byEventName")
    public ResponseEntity<List<EventDTO>> searchEventsByName(@RequestParam String name) {
        // search events for name event
        Optional<List<EventDTO>> eventsOpt = eventService.findEventsByEventNameContaining(name);

        // Gestione della risposta
        if (eventsOpt.isPresent() && !eventsOpt.get().isEmpty()) {
            return ResponseEntity.ok(eventsOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/byGameName")
    public ResponseEntity<List<EventDTO>> searchEventsByGameName(@RequestParam String gameName) {
        // search event for name game
        Optional<List<EventDTO>> eventsOpt = eventService.findEventsByGameNameContaining(gameName);

        // Gestione della risposta
        if (eventsOpt.isPresent() && !eventsOpt.get().isEmpty()) {
            return ResponseEntity.ok(eventsOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}