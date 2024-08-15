package co.develhope.team2.meeplemeet_project_team2.controllers;


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
    public ResponseEntity<Event> create(@RequestParam Integer userId, @RequestBody Event event ) {
        try {
           Event createEvent = eventService.createEvent(userId,event);
            return ResponseEntity.ok(createEvent);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Event>> getList() {
        List<Event> eventList = eventService.getAllEvent();
        return ResponseEntity.ok(eventList);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Optional<Event>> getWithId(@PathVariable Integer id) {
        Optional<Event> event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Event> update(@PathVariable Integer id, @RequestBody @NotNull Event event ) {
        Event updateEvent = eventService.updateEvent(id,event);
        return ResponseEntity.ok(updateEvent);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Event> delete(@PathVariable Integer id) {
        Event deletedEvent = eventService.deleteEventById(id);
        return ResponseEntity.ok(deletedEvent);
    }
    @PutMapping("/setStatus/{id}")
    public ResponseEntity<Event> setStatusEvent(@PathVariable Integer id, @NotNull Event event){

        try {
            Event updateEvent = eventService.terminatedEvent(id);
            return ResponseEntity.ok(updateEvent);
        }catch (IllegalArgumentException e){
           return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/enrollments/users/{id}")
    public ResponseEntity<List<User>> usersEnrolled(@PathVariable Integer id){
        Optional<Event> event = eventService.getEventById(id);
        List<User> users = event.get().getUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/enrollments/event/{eventId}/{userId}")
    public ResponseEntity<Event> eventsUsers(@PathVariable Integer eventId, @PathVariable Integer userId){
        Event event = eventService.usersEnrolled(userId, eventId);
        return ResponseEntity.ok(event);
    }
}

