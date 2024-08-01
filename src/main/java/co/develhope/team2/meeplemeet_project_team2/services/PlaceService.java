package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.PlaceDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceStatus;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
        Optional<Place> place = placeRepository.findById(id);
        if (place.isPresent()) {
            if (place.get().getRecordStatusPlace() == RecordStatus.ACTIVE) {
                return placeRepository.findById(id);
            } else {
                throw new EntityNotFoundException("The place with id: " + id + " is deleted");
            }
        } else {
            throw new EntityNotFoundException("The place with id:" + id + " doesn't exist");
        }
    }

    public Place updatePlace(Integer id, Place updatePlace) {

        Optional<Place> placeOptional = placeRepository.findById(id);

        if (placeOptional.isPresent()) {
            placeRepository.save(updatePlace);
        } else {
            throw new EntityNotFoundException("Place with id: " + id + " doesn't exist");
        }
        return updatePlace;
    }

    public Place deleteLogicalPlace(Integer id, Place deletePlace) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if(placeOptional.isPresent()) {
            deletePlace.setRecordStatusPlace(RecordStatus.DELETED);
            placeRepository.save(deletePlace);
        } else {
            throw new EntityNotFoundException("Place with id: " + id + " doesn't exist");
        }
        return deletePlace;
    }

    public void deletePlaceById(Integer id) {
        placeRepository.deleteById(id);
    }

    public void deleteAllPlace() {
        placeRepository.deleteAll();
    }

    public List<Place> findOpenPlace() {
        LocalTime now = LocalTime.now();
        for (Place place : placeRepository.findAll()) {
            if (place.getOpening().isBefore(now) && place.getClosing().isAfter(now)) {
                return placeRepository.isOpen(now);
            }
        }
        return null;
    }
}
