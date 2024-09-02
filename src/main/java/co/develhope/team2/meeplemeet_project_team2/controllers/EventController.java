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
    public ResponseEntity<Optional<Event>> create(@RequestParam(name = "user") Integer userId, @RequestParam(name = "place") Integer placeId, @RequestBody Event event ) {
        try {
           Optional<Event> createEvent = eventService.createEvent(userId, placeId, event);
            return ResponseEntity.ok(createEvent);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Optional<List<Event>>> getList() {
        Optional<List<Event>> eventList = eventService.getAllEvent();
        return ResponseEntity.ok(eventList);
    }
    @GetMapping("/deleted/list")
    public ResponseEntity<Optional<List<Event>>> getListDeleted() {
        Optional<List<Event>> eventList = eventService.getAllEventDeleted();
        return ResponseEntity.ok(eventList);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Optional<Event>> getWithId(@PathVariable Integer id) {
        Optional<Event> event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<Event>> update(@PathVariable Integer id, @RequestBody @NotNull Event event ) {
        Optional<Event> updateEvent = eventService.updateEvent(id,event);
        return ResponseEntity.ok(updateEvent);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Optional<Event>> delete(@PathVariable Integer id) {
        Optional<Event> deletedEvent = eventService.deleteEventById(id);
        return ResponseEntity.ok(deletedEvent);
    }
    @DeleteMapping("/delete/logic/{id}")
    public ResponseEntity<Event> logicDeletion(@PathVariable Integer id) {
        Optional<Event> deletedEvent = eventService.logicDeletion(id);
        return ResponseEntity.ok(deletedEvent.get());
    }

    //todo: sistemare event non usato
    @PutMapping("/setStatus/{id}")
    public ResponseEntity<Optional<Event>> setStatusEvent(@PathVariable Integer id, @NotNull Event event){

        try {
            Optional<Event> updateEvent = eventService.terminatedEvent(id);
            return ResponseEntity.ok(updateEvent);
        }catch (IllegalArgumentException e){
           return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/enrollments")
    public ResponseEntity<Event> eventsUsers(@RequestParam(name = "userID") Integer userId, @RequestParam(name = "eventID") Integer eventId){
        eventService.usersEnrolled(userId, eventId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unsubscribe")
    public ResponseEntity<Event> unsuscribeUser(@RequestParam(name = "userID") Integer userId, @RequestParam(name = "eventID") Integer eventId){
        eventService.userUnsubscribe(userId, eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/enrollments/user")
    public ResponseEntity<Optional<List<User>>> listOfPartecipants(@RequestParam(name = "eventID") Integer eventId) {
        Optional<List<User>> user = eventService.listOfUserParticipateEvent(eventId);
        return ResponseEntity.ok(user);
    }
}