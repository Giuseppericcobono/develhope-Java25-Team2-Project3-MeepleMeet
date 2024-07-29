package co.develhope.team2.meeplemeet_project_team2.controllers;


import co.develhope.team2.meeplemeet_project_team2.services.EventService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.repositories.EventRepository;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Event> create(@RequestBody Event event) {
        Event newEvent = eventService.createEvent(event);
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }
    @GetMapping("/list")
    public @ResponseBody ResponseEntity<List<Event>> getList() {
        List<Event> eventList = eventService.getAllEvent();
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public @ResponseBody ResponseEntity<Optional<Event>> getWithId(@PathVariable Integer id) {
        Optional<Event> event = eventService.getEventById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public @ResponseBody ResponseEntity<Event> update(@PathVariable Integer id, @RequestBody @NotNull Event event ) {
        Event updateEvent = eventService.updateEvent(id,event);
        return new ResponseEntity<>(updateEvent,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Event> delete(@PathVariable Integer id) {
        Event deletedEvent = eventService.deleteEventById(id);
        return new ResponseEntity<>(deletedEvent, HttpStatus.OK);
    }
}

