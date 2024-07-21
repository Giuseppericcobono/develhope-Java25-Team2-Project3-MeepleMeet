package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.repository.PlaceRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/place")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @PostMapping("/create")
    public @ResponseBody Place createPlace(@RequestBody Place place) {
        return placeRepository.save(place);
    }

    @GetMapping("/search/list")
    public @ResponseBody List<Place> placeList() {
        return placeRepository.findAll();
    }

    @GetMapping("/search/{id}")
    public @ResponseBody Place searchPlace(@PathVariable Integer id) {
        Optional<Place> place = placeRepository.findById(id);
        return place.orElse(null);
    }

    @PutMapping("/update/{id}")
    public @ResponseBody Place updatePlace(@PathVariable Integer id, @RequestBody @NotNull Place place) {
        place.setId(id);
        return placeRepository.save(place);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePlace(@PathVariable Integer id) {
        placeRepository.deleteById(id);
    }

    @DeleteMapping("/delete/all")
    public void deleteAllPlace() {
        placeRepository.deleteAll();
    }
}
