package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.repositories.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public Place createAPlace(Place place) {
        return placeRepository.save(place);
    }

    public List<Place> getListOfPlaces() {
        return placeRepository.findAll();
    }

    public Optional<Place> getPlaceById(Integer id) {
        return placeRepository.findById(id);
    }

    public Place updatePlace(Integer id, Place updatePlace) {

        Optional<Place> placeOptional = placeRepository.findById(id);

        if(placeOptional.isPresent()){
            placeRepository.save(updatePlace);
        } else {
            throw new EntityNotFoundException("Place with id " + id + " not found");
        }
        return updatePlace;
    }

    public void deletePlaceById(Integer id) {
        placeRepository.deleteById(id);
    }

    public void deleteAllPlace() {
        placeRepository.deleteAll();
    }
}
