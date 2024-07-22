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
    public @ResponseBody Event createUser(@RequestBody Event event){
        return eventRepository.save(event);
    }

    @GetMapping("/list")
    public @ResponseBody List<Event> getList() {
        return eventRepository.findAll();
    }
    @GetMapping("/{id}")
    public @ResponseBody Event getWithId(@PathVariable Integer id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.orElse(null);
    }
    @PutMapping("/{id}")
    public @ResponseBody Event update(@PathVariable Integer id, @RequestBody @NotNull Event event) {
        event.setId(id);
        return eventRepository.save(event);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        eventRepository.deleteById(id);
    }
}

