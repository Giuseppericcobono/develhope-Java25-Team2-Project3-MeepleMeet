package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

//    @PatchMapping
//    public ResponseEntity<Event> createEvent(@RequestBody Event event){
//        Event eventSaves = eventRepository.save(event);
//        return new ResponseEntity<>(eventSaves, HttpStatus.CREATED);
//    }
//    @GetMapping
//    public List<Event>getAllEvents(){
//        return eventRepository.findAll();
//    }
}

