package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.services.PlaceService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    // create a new place
    @PostMapping("/create")
    public ResponseEntity<Optional<Place>> createPlace(@RequestBody Place place) {
        Optional<Place> newPlace = placeService.createAPlace(place);
        return ResponseEntity.ok(newPlace);
    }

    // searches for places based on status (Active, deleted or all)
    @GetMapping("/search/list/{status}")
    public ResponseEntity<Optional<List<Place>>> placeList(@PathVariable String status) {
        Optional<List<Place>> placeList = placeService.getListOfPlaces(status);
        return ResponseEntity.ok(placeList);
    }

    // searches for places based on placetype (Public or private)
    @GetMapping("/search/list/type/{placeType}")
    public ResponseEntity<Optional<List<Place>>> placeTypeList(@PathVariable String placeType) {
        Optional<List<Place>> listPlaces = placeService.getListPlaceType(placeType);
        return ResponseEntity.ok(listPlaces);
    }

    // searches for places based on the current time
    @GetMapping("/search/time/now")
    public ResponseEntity<Optional<List<Place>>> placeTimeList() {
        Optional<List<Place>> listTimePlace = placeService.findOpenPlaceNow();
        return ResponseEntity.ok(listTimePlace);
    }

    // searches for places based on a specific time
    @GetMapping("/search/time")
    public ResponseEntity<Optional<List<Place>>> placeTimeList2(@RequestParam(name = "at") LocalTime time) {
        Optional<List<Place>> listTimePlace2 = placeService.findOpenPlace(time);
        return ResponseEntity.ok(listTimePlace2);
    }

    // search for place ID
    @GetMapping("/search/{id}")
    public @ResponseBody ResponseEntity<Optional<Place>> searchPlace(@PathVariable Integer id) {
        Optional<Place> idplace = placeService.getActivePlaceById(id);
        return ResponseEntity.ok(idplace);
    }

    // searches for places based on the name
    @GetMapping("/search/n")
    public ResponseEntity<Optional<List<Place>>> searchPlaceByName(@RequestParam(name = "name") String name) {
        Optional<List<Place>> searchBy = placeService.getPlaceByName(name);
        return ResponseEntity.ok(searchBy);
    }

    // searches for places based on address
    @GetMapping("/search/a")
    public ResponseEntity<Optional<List<Place>>> searchPlaceByAddress(@RequestParam(name = "address") String address) {
        Optional<List<Place>> searchBy = placeService.getPlaceByAddress(address);
        return ResponseEntity.ok(searchBy);
    }

    // update information about a place by selecting it with its id
    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<Place>> updatePlace(@PathVariable Integer id, @RequestBody @NotNull Place place) {
        Optional<Place> updatePlace = placeService.updatePlace(id, place);
        return ResponseEntity.ok(updatePlace);
    }

    // reactivation of a deleted place
    @PatchMapping("/reactive/{id}")
    public ResponseEntity<Void> reactivationPlace(@PathVariable Integer id) {
        Optional<Place> idPlace = placeService.getPlaceById(id);
        placeService.reactivationOfAPlace(id, idPlace.get());
        return ResponseEntity.ok().build();
    }

    // logical delete of a place
    @DeleteMapping("delete/l/{id}")
    public ResponseEntity<Place> deleteLogicalPlace(@PathVariable Integer id) {
        Optional<Place> idPlace = placeService.getActivePlaceById(id);
        placeService.deleteLogicalPlace(id, idPlace.get());
        return ResponseEntity.ok().build();
    }

    // delete a place by selecting it with its id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Place> deletePlace(@PathVariable Integer id) {
        placeService.deletePlaceById(id);
        return ResponseEntity.ok().build();
    }

    // delete all places
    @DeleteMapping("/delete/all")
    public ResponseEntity<Place> deleteAllPlace() {
        placeService.deleteAllPlace();
        return ResponseEntity.ok().build();
    }
}
