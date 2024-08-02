package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
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

    public List<Place> getListOfActivePlaces() {
        return placeRepository.statusEntity(RecordStatus.ACTIVE);
    }

    public List<Place> getListOfDeletedPlaces() {
        return placeRepository.statusEntity(RecordStatus.DELETED);
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

    public Optional<Place> getPlaceByName(String name) {
        Optional<Place> place = placeRepository.findByName(name);
        if(place.isPresent()) {
            if(place.get().getRecordStatusPlace() == RecordStatus.ACTIVE) {
                return placeRepository.findByName(name);
            } else {
                throw new EntityNotFoundException("The place with name: " + name + " is deleted");
            }
        } else {
            throw new EntityNotFoundException("The place with name: " + name + " doesn't exist");
        }
    }

    public Optional<Place> getPlaceByAddress(String address) {
        Optional<Place> place = placeRepository.findByAdress(address);
        if(place.isPresent()){
            if(place.get().getRecordStatusPlace() == RecordStatus.ACTIVE) {
                return placeRepository.findByAdress(address);
            } else {
                throw new EntityNotFoundException("The place with address: " + address + " is deleted");
            }
        } else {
            throw new EntityNotFoundException("The place with address: " + address + " doesn't exist");
        }
    }

    public Place updatePlace(Integer id, Place updatePlace) {

        Optional<Place> placeOptional = placeRepository.findById(id);

        if (placeOptional.isPresent()) {
            if (placeOptional.get().getRecordStatusPlace() == RecordStatus.ACTIVE) {
                return placeRepository.save(updatePlace);
            } else {
                throw new EntityNotFoundException("Place with id: " + id + " is deleted");
            }
        } else {
            throw new EntityNotFoundException("Place with id: " + id + " doesn't exist");
        }
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

    public List<Place> findOpenPlace (LocalTime time) {
        for (Place place : placeRepository.findAll()) {
            if(place.getOpening().isBefore(time) && place.getClosing().isAfter(time)) {
                return placeRepository.isOpen(time);
            }
        }
        return null;
    }

    public List<Place> findOpenPlaceNow() {
        LocalTime now = LocalTime.now();
        for (Place place : placeRepository.findAll()) {
            if (place.getOpening().isBefore(now) && place.getClosing().isAfter(now)) {
                return placeRepository.isOpen(now);
            }
        }
        return null;
    }
}
