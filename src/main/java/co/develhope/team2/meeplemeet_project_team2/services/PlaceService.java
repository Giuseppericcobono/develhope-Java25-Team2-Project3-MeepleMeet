package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.PublicPlace;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Transactional
    public Place createAPlace(Place place) {
        if (place.getPlaceType() == PlaceType.PUBLIC) {
            PublicPlace publicPlace = new PublicPlace();
            publicPlace.setId(place.getId());
            publicPlace.setName(publicPlace.getName());
            publicPlace.setMaxCapacity(publicPlace.getMaxCapacity());
            publicPlace.setOpening(publicPlace.getOpening());
            publicPlace.setClosing(publicPlace.getClosing());
        } else {
            place.setPublicPlace(null);
        }
        return placeRepository.save(place);
    }

    public List<Place> getListOfPlaces(String status) {
        List<Place> places;
        switch (status) {
            case "active" -> {
                places = placeRepository.statusEntity(RecordStatus.ACTIVE);
                return places;
            }
            case "deleted" -> {
                places = placeRepository.statusEntity(RecordStatus.DELETED);
                return places;
            }
            case "all" -> {
                places = placeRepository.findAll();
                return places;
            }
            default -> {
                return null;
            }
        }
    }

    public List<Place> getListPlacetype(String placeType) {
        List<Place> listPlaces;
        if (placeType.equals("public")) {
            listPlaces = placeRepository.findPlaceType(PlaceType.PUBLIC);
            return listPlaces;
        } else if (placeType.equals("private")) {
            listPlaces = placeRepository.findPlaceType(PlaceType.PRIVATE);
            return listPlaces;
        } else {
            return null;
        }
    }

    public Optional<Place> getPlaceById(Integer id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isPresent()) {
            return placeRepository.findById(id);
        } else {
            throw new EntityNotFoundException("The place with id:" + id + " doesn't exist");
        }
    }

    public Optional<Place> getActivePlaceById(Integer id) {
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

    public List<Place> getPlaceByName(String name) {
        List<Place> places = placeRepository.findActivePlacesByPublicPlaceName(name);
        if (places.isEmpty()) {
            throw new EntityNotFoundException("The place with name: " + name + " doesn't exist");
        }
        return places;
    }

    public List<Place> getPlaceByAddress(String address) {
        List<Place> places = placeRepository.findByAdress(address);
        if (places.isEmpty()) {
            throw new EntityNotFoundException("The place with address: " + address + " doesn't exist");
        }
        List<Place> activePlaces = places.stream().filter(place -> place.getRecordStatusPlace() == RecordStatus.ACTIVE).toList();
        if (activePlaces.isEmpty()) {
            throw new EntityNotFoundException("The place with address: " + address + " is deleted");
        }
        return activePlaces;
    }

    public Place updatePlace(Integer id, Place updatePlace) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isPresent()) {
            if (placeOptional.get().getRecordStatusPlace() == RecordStatus.ACTIVE) {
                return placeRepository.saveAndFlush(updatePlace);
            } else {
                throw new EntityNotFoundException("Place with id: " + id + " is deleted");
            }
        } else {
            throw new EntityNotFoundException("Place with id: " + id + " doesn't exist");
        }
    }

    public void reactivationOfAPlace(Integer id, Place reactivePlace) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isPresent()) {
            reactivePlace.setRecordStatusPlace(RecordStatus.ACTIVE);
            placeRepository.saveAndFlush(reactivePlace);
        } else {
            throw new EntityNotFoundException("Place with id: " + id + " doesn't exist");
        }
    }

    public void deleteLogicalPlace(Integer id, Place deletePlace) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isPresent()) {
            deletePlace.setRecordStatusPlace(RecordStatus.DELETED);
            placeRepository.save(deletePlace);
        } else {
            throw new EntityNotFoundException("Place with id: " + id + " doesn't exist");
        }
    }

    public void deletePlaceById(Integer id) {
        placeRepository.deleteById(id);
    }

    public void deleteAllPlace() {
        placeRepository.deleteAll();
    }

    public List<Place> findOpenPlace(LocalTime time) {
        for (Place place : placeRepository.findAll()) {
            if (place.getPublicPlace().getOpening().isBefore(time) && place.getPublicPlace().getClosing().isAfter(time)) {
                return placeRepository.isOpen(time);
            }
        }
        return null;
    }

    public List<Place> findOpenPlaceNow() {
        LocalTime now = LocalTime.now();
        for (Place place : placeRepository.findAll()) {
            if (place.getPublicPlace().getOpening().isBefore(now) && place.getPublicPlace().getClosing().isAfter(now)) {
                return placeRepository.isOpen(now);
            }
        }
        return null;
    }
}
