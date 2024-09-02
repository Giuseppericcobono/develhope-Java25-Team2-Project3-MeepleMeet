package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.PlaceRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    Logger logger = LoggerFactory.getLogger(PlaceService.class);

    // method of creating a place
    @Transactional
    public Optional<Place> createAPlace(Place place) {
        placeRepository.save(place);
        return Optional.of(place);
    }

    // method to return a list of places based on a status
    public Optional<List<Place>> getListOfPlaces(String status) {
        Optional<List<Place>> places;
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
                places = Optional.of(placeRepository.findAll());
                return places;
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    // method to return a list of places based on a place type
    public Optional<List<Place>> getListPlaceType(String placeType) {
        Optional<List<Place>> listPlaces;
        if (placeType.equals("public")) {
            listPlaces = placeRepository.findPlaceType(PlaceType.PUBLIC);
            return listPlaces;
        } else if (placeType.equals("private")) {
            listPlaces = placeRepository.findPlaceType(PlaceType.PRIVATE);
            return listPlaces;
        } else {
            return Optional.empty();
        }
    }

    // method to find a place by id
    public Optional<Place> getPlaceById(Integer id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isPresent()) {
            return placeRepository.findById(id);
        } else {
            logger.info("The place with id:" + id + " doesn't exist");
            return Optional.empty();
        }
    }

    // method to find an active or inactive place by id
    public Optional<Place> getActivePlaceById(Integer id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isPresent()) {
            if (place.get().getRecordStatusPlace() == RecordStatus.ACTIVE || place.get().getRecordStatusPlace() == RecordStatus.INACTIVE) {
                return placeRepository.findById(id);
            } else {
                logger.info("The place with id: " + id + " is deleted");
                return Optional.empty();
            }
        } else {
            logger.info("The place with id:" + id + " doesn't exist");
            return Optional.empty();
        }
    }

    // method to find a place by name
    public Optional<List<Place>> getPlaceByName(String name) {
        Optional<List<Place>> places = placeRepository.findByName(name);
        if (places.isEmpty()) {
            logger.info("The place with name: " + name + " doesn't exist");
            return Optional.empty();
        }
        return places;
    }

    // method to find a place by address
    public Optional<List<Place>> getPlaceByAddress(String address) {
        Optional<List<Place>> places = placeRepository.findByAddress(address);
        if (places.isEmpty()) {
            logger.info("The place with address: " + address + " doesn't exist");
            return Optional.empty();
        }
        return places;
    }

    // method to update the information of a place based on id
    public Optional<Place> updatePlace(Integer id, Place updatePlace) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isPresent()) {
            Place existingPlace = placeOptional.get();
            if (placeOptional.get().getRecordStatusPlace() == RecordStatus.ACTIVE) {
                existingPlace.setAddress(updatePlace.getAddress());
                existingPlace.setPlaceType(updatePlace.getPlaceType());
                existingPlace.setInfo(updatePlace.getInfo());
                existingPlace.setName(updatePlace.getName());
                existingPlace.setMaxCapacity(updatePlace.getMaxCapacity());
                existingPlace.setOpening(updatePlace.getOpening());
                existingPlace.setClosing(updatePlace.getClosing());

                placeRepository.save(existingPlace);
                return Optional.of(existingPlace);
            } else {
                logger.info("Place with id: " + id + " is deleted");
                return Optional.empty();
            }
        } else {
            logger.info("Place with id: " + id + " doesn't exist");
            return Optional.empty();
        }
    }

    // method to reactivation a deleted or inactive place
    public void reactivationOfAPlace(Integer id, Place reactivePlace) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isPresent()) {
            reactivePlace.setRecordStatusPlace(RecordStatus.ACTIVE);
            placeRepository.saveAndFlush(reactivePlace);
        } else {
            logger.info("Place with id: " + id + " doesn't exist");
        }
    }

    // method to set the status of a place to deleted (logical delete)
    public void deleteLogicalPlace(Integer id, Place deletePlace) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isPresent()) {
            deletePlace.setRecordStatusPlace(RecordStatus.DELETED);
            placeRepository.save(deletePlace);
        } else {
            logger.info("Place with id: " + id + " doesn't exist");
        }
    }

    // method to delete a place by id
    public void deletePlaceById(Integer id) {
        placeRepository.deleteById(id);
    }

    // method to delete all places
    public void deleteAllPlace() {
        placeRepository.deleteAll();
    }

    // method to find a list of places based on a specific time
    public Optional<List<Place>> findOpenPlace(LocalTime time) {
        for (Place place : placeRepository.findAll()) {
            if (place.getOpening().isBefore(time) && place.getClosing().isAfter(time)) {
                return placeRepository.isOpen(time);
            }
        }
        logger.info("There are no open place at: " + time);
        return Optional.empty();
    }

    // method to find a list of places based on a current time
    public Optional<List<Place>> findOpenPlaceNow() {
        LocalTime now = LocalTime.now();
        for (Place place : placeRepository.findAll()) {
            if (place.getOpening().isBefore(now) && place.getClosing().isAfter(now)) {
                return placeRepository.isOpen(now);
            }
        }
        logger.info("There are no open place now");
        return Optional.empty();
    }
}
