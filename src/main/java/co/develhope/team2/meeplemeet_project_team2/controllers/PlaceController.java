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

    @PostMapping("/create")
    public ResponseEntity<Place> createPlace(@RequestBody Place place) {
        Place newPlace = placeService.createAPlace(place);
        return ResponseEntity.ok(newPlace);
    }

    @GetMapping("/search/list/{status}")
    public ResponseEntity<List<Place>> placeList(@PathVariable String status) {
        List<Place> placeList = placeService.getListOfPlaces(status);
        return ResponseEntity.ok(placeList);
    }

    @GetMapping("/search/list/type/{placeType}")
    public ResponseEntity<List<Place>> placeTypeList(@PathVariable String placeType) {
        List<Place> listPlaces = placeService.getListPlaceType(placeType);
        return ResponseEntity.ok(listPlaces);
    }

    @GetMapping("/search/time/now")
    public ResponseEntity<List<Place>> placeTimeList() {
        List<Place> listTimePlace = placeService.findOpenPlaceNow();
        return ResponseEntity.ok(listTimePlace);
    }

    @GetMapping("/search/time")
    public ResponseEntity<List<Place>> placeTimeList2(@RequestParam(name = "at") LocalTime time) {
        List<Place> listTimePlace2 = placeService.findOpenPlace(time);
        return ResponseEntity.ok(listTimePlace2);
    }

    @GetMapping("/search/{id}")
    public @ResponseBody ResponseEntity<Optional<Place>> searchPlace(@PathVariable Integer id) {
        Optional<Place> idplace = placeService.getActivePlaceById(id);
        return ResponseEntity.ok(idplace);
    }

    @GetMapping("/search/n")
    public ResponseEntity<List<Place>> searchPlaceByName(@RequestParam(name = "name") String name) {
        List<Place> searchBy = placeService.getPlaceByName(name);
        return ResponseEntity.ok(searchBy);
    }

    @GetMapping("/search/a")
    public ResponseEntity<List<Place>> searchPlaceByAddress(@RequestParam(name = "address") String address) {
        List<Place> searchBy = placeService.getPlaceByAddress(address);
        return ResponseEntity.ok(searchBy);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Integer id, @RequestBody @NotNull Place place) {
        Place updatePlace = placeService.updatePlace(id, place);
        return ResponseEntity.ok(updatePlace);
    }

    @PatchMapping("/reactive/{id}")
    public ResponseEntity<Void> reactivationPlace(@PathVariable Integer id) {
        Optional<Place> idPlace = placeService.getPlaceById(id);
        placeService.reactivationOfAPlace(id, idPlace.get());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/l/{id}")
    public ResponseEntity<Place> deleteLogicalPlace(@PathVariable Integer id) {
        Optional<Place> idPlace = placeService.getActivePlaceById(id);
        placeService.deleteLogicalPlace(id, idPlace.get());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Place> deletePlace(@PathVariable Integer id) {
        placeService.deletePlaceById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Place> deleteAllPlace() {
        placeService.deleteAllPlace();
        return ResponseEntity.ok().build();
    }
}
