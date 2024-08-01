package co.develhope.team2.meeplemeet_project_team2.controllers;


import co.develhope.team2.meeplemeet_project_team2.DTO.EventDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.EventStatusEnum;
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
    public ResponseEntity<EventDTO> create(@RequestParam Integer userID, @RequestBody EventDTO eventDTO) {
        try {
            eventDTO.setEventStatusEnum(EventStatusEnum.NOT_STARTED);

            // Chiama il servizio per creare l'evento
            EventDTO newEvent = eventService.createEvent(userID, eventDTO);

            // Restituisce una risposta HTTP con lo stato CREATED
            return new ResponseEntity<>(newEvent, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Gestisci il caso in cui l'utente non è trovato
            // Puoi restituire un errore specifico o un messaggio
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
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

