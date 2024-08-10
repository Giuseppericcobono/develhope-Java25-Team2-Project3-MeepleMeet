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
        List<Place> listPlaces;
        if (status.equals("active")) {
            listPlaces = placeService.getListOfActivePlaces();
            return ResponseEntity.ok(listPlaces);
        } else if (status.equals("deleted")) {
            listPlaces = placeService.getListOfDeletedPlaces();
            return ResponseEntity.ok(listPlaces);
        } else if (status.equals("all")) {
            listPlaces = placeService.getListOfPlaces();
            return ResponseEntity.ok(listPlaces);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search/list/type/{placeType}")
    public ResponseEntity<List<Place>> placeTypeList(@PathVariable String placeType) {
        List<Place> listPlaces;
        if (placeType.equals("public")) {
            listPlaces = placeService.getListOfPublicPlace();
            return ResponseEntity.ok(listPlaces);
        } else if (placeType.equals("private")) {
            listPlaces = placeService.getListOfPrivatePlace();
            return ResponseEntity.ok(listPlaces);
        } else {
            return ResponseEntity.badRequest().build();
        }
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
        Optional<Place> idplace = placeService.getPlaceById(id);
        if(idplace.isPresent()) {
        return ResponseEntity.ok(idplace);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search/n")
    public ResponseEntity<List<Place>> searchPlaceByName(@RequestParam (name = "name") String name) {
        List<Place> searchBy = placeService.getPlaceByName(name);
        if(!searchBy.isEmpty()) {
            return ResponseEntity.ok(searchBy);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search/a")
    public ResponseEntity<List<Place>> searchPlaceByAdress(@RequestParam (name = "address") String address) {
        List<Place> searchBy = placeService.getPlaceByAddress(address);
        if(!searchBy.isEmpty()) {
            return ResponseEntity.ok(searchBy);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Integer id, @RequestBody @NotNull Place place) {
        Optional<Place> idPlace = placeService.getPlaceById(id);
        if(idPlace.isPresent()) {
            Place updatePlace = placeService.updatePlace(id, place);
        return ResponseEntity.ok(updatePlace);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/l/{id}")
    public ResponseEntity<Place> deleteLogicalPlace(@PathVariable Integer id) {
        Optional<Place> idPlace = placeService.getPlaceById(id);
        if(idPlace.isPresent()) {
            placeService.deleteLogicalPlace(id, idPlace.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Place> deletePlace(@PathVariable Integer id) {
        Optional<Place> idPlace = placeService.getPlaceById(id);
        if(idPlace.isPresent()){
        placeService.deletePlaceById(id);
        return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Place> deleteAllPlace() {
        placeService.deleteAllPlace();
        return ResponseEntity.ok().build();
    }
}
