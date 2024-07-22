package co.develhope.team2.meeplemeet_project_team2.controllers;


import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.repositories.EventRepository;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/create")
    public @ResponseBody Event createEvent(@RequestBody Event event){
        return eventRepository.save(event);
    }

    @GetMapping("/search/list")
    public @ResponseBody List<Event> getList() {
        return eventRepository.findAll();
    }

    @GetMapping("/search/{id}")
    public @ResponseBody Event getWithId(@PathVariable Integer id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.orElse(null);
    }
    @PutMapping("/update/{id}")
    public @ResponseBody Event update(@PathVariable Integer id, @RequestBody @NotNull Event event) {
        event.setId(id);
        return eventRepository.save(event);
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        eventRepository.deleteById(id);
    }

    @DeleteMapping("/delete/all")
    public void deleteAllEvent() {
        eventRepository.deleteAll();
    }
}

