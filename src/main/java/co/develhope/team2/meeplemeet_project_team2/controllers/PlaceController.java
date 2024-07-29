package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;

import co.develhope.team2.meeplemeet_project_team2.services.PlaceService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(newPlace, HttpStatus.CREATED);
    }

    @GetMapping("/search/list")
    public @ResponseBody ResponseEntity<List<Place>> placeList() {
        List<Place> ListPlace = placeService.getListOfPlaces();
        return new ResponseEntity<>(ListPlace, HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public @ResponseBody ResponseEntity<Optional<Place>> searchPlace(@PathVariable Integer id) {
        Optional<Place> place = placeService.getPlaceById(id);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public @ResponseBody ResponseEntity<Place> updatePlace(@PathVariable Integer id, @RequestBody @NotNull Place place) {
        Place updatePlace = placeService.updatePlace(id, place);
        return new ResponseEntity<>(updatePlace, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public @ResponseBody ResponseEntity<Place> deletePlace(@PathVariable Integer id) {
        placeService.deletePlaceById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/all")
    public @ResponseBody ResponseEntity<Place> deleteAllPlace() {
        placeService.deleteAllPlace();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
