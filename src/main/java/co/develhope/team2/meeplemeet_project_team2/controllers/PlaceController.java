package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.DTO.PlaceDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Place;

import co.develhope.team2.meeplemeet_project_team2.services.PlaceService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @PostMapping("/create")
    public ResponseEntity<Place> createPlace(@RequestBody Place place) {
        Place newPlace = placeService.createAPlace(place);
        return ResponseEntity.ok(newPlace);
    }

    @GetMapping("/search/list")
    public @ResponseBody ResponseEntity<List<Place>> placeList() {
        List<Place> listPlace = placeService.getListOfPlaces();
        return ResponseEntity.ok(listPlace);
    }

    @GetMapping("/search/currenttime")
    public @ResponseBody ResponseEntity<List<Place>> placeTimeList() {
        List<Place> listTimePlace = placeService.findOpenPlace();
        return ResponseEntity.ok(listTimePlace);
    }

    @GetMapping("/search/{id}")
    public @ResponseBody ResponseEntity<Optional<Place>> searchPlace(@PathVariable Integer id) {
        Optional<Place> idplace = placeService.getPlaceById(id);
        if(idplace.isPresent()) {
        return ResponseEntity.ok(idplace);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public @ResponseBody ResponseEntity<Place> updatePlace(@PathVariable Integer id, @RequestBody @NotNull Place place) {
        Optional<Place> idPlace = placeService.getPlaceById(id);
        if(idPlace.isPresent()) {
            Place updatePlace = placeService.updatePlace(id, place);
        return ResponseEntity.ok(updatePlace);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/l/{id}")
    public @ResponseBody ResponseEntity<Place> deleteLogicalPlace(@PathVariable Integer id) {
        Optional<Place> idPlace = placeService.getPlaceById(id);
        if(idPlace.isPresent()) {
            placeService.deleteLogicalPlace(id, idPlace.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public @ResponseBody ResponseEntity<Place> deletePlace(@PathVariable Integer id) {
        Optional<Place> idPlace = placeService.getPlaceById(id);
        if(idPlace.isPresent()){
        placeService.deletePlaceById(id);
        return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/all")
    public @ResponseBody ResponseEntity<Place> deleteAllPlace() {
        placeService.deleteAllPlace();
        return ResponseEntity.ok().build();
    }
}
