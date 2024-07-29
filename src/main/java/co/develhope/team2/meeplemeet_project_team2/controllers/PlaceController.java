package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;

import co.develhope.team2.meeplemeet_project_team2.services.PlaceService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @PostMapping("/create")
    public @ResponseBody Place createPlace(@RequestBody Place place) {
        return placeService.createAPlace(place);
    }

    @GetMapping("/search/list")
    public @ResponseBody List<Place> placeList() {
        return placeService.getListOfPlaces();
    }

    @GetMapping("/search/{id}")
    public @ResponseBody Place searchPlace(@PathVariable Integer id) {
        placeService.getPlaceById(id);
    }

    @PutMapping("/update/{id}")
    public @ResponseBody Place updatePlace(@PathVariable Integer id, @RequestBody @NotNull Place place) {
        placeService.updatePlace(id, place);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePlace(@PathVariable Integer id) {
        placeService.deletePlaceById(id);
    }

    @DeleteMapping("/delete/all")
    public void deleteAllPlace() {
        placeService.deleteAllPlace();
    }
}
