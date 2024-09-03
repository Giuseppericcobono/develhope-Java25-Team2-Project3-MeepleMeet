package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.PlaceDTO;
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
import java.util.ArrayList;
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
        place.setRecordStatusPlace(RecordStatus.ACTIVE);
        placeRepository.save(place);
        return Optional.of(place);
    }

    public PlaceDTO createPlaceDto(Place place) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setName(place.getName());
        placeDTO.setAddress(place.getAddress());
        placeDTO.setInfo(place.getInfo());
        placeDTO.setOpening(place.getOpening());
        placeDTO.setClosing(place.getClosing());
        placeDTO.setMaxCapacity(place.getMaxCapacity());
        return placeDTO;
    }

    public List<PlaceDTO> listOfPlacesDTO(List<Place> places){
        List<PlaceDTO> placeDTOList = new ArrayList<>();
        for(Place p : places) {
            PlaceDTO placeDTO = new PlaceDTO();
            placeDTO.setName(p.getName());
            placeDTO.setAddress(p.getAddress());
            placeDTO.setInfo(p.getInfo());
            placeDTO.setOpening(p.getOpening());
            placeDTO.setClosing(p.getClosing());
            placeDTO.setMaxCapacity(p.getMaxCapacity());
            placeDTOList.add(placeDTO);
        }
        return placeDTOList;
    }

    // method to return a list of places based on a status
    public Optional<List<Place>> getListOfPlaces(String status) {
        List<Place> places;
        switch (status) {
            case "active" -> {
                places = placeRepository.statusEntity(RecordStatus.ACTIVE);
                return Optional.of(places);
            }
            case "deleted" -> {
                places = placeRepository.statusEntity(RecordStatus.DELETED);
                return Optional.of(places);
            }
            case "all" -> {
                places = placeRepository.findAll();
                return Optional.of(places);
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    // method to return a list of places based on a place type
    public Optional<List<Place>> getListPlaceType(String placeType) {
        List<Place> listPlaces;
        if (placeType.equals("public")) {
            listPlaces = placeRepository.findPlaceType(PlaceType.PUBLIC);
            return Optional.of(listPlaces);
        } else if (placeType.equals("private")) {
            listPlaces = placeRepository.findPlaceType(PlaceType.PRIVATE);
            return Optional.of(listPlaces);
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
    public Optional<List<PlaceDTO>> getPlaceByName(String name) {
        List<Place> places = placeRepository.findByName(name);
        Optional<List<PlaceDTO>> placeDTOList = Optional.of(listOfPlacesDTO(places));
        if (places.isEmpty()) {
            logger.info("The place with name: " + name + " doesn't exist");
            return Optional.empty();
        }
        return placeDTOList;
    }

    // method to find a place by address
    public Optional<List<PlaceDTO>> getPlaceByAddress(String address) {
        List<Place> places = placeRepository.findByAddress(address);
        Optional<List<PlaceDTO>> placeDTOList = Optional.of(listOfPlacesDTO(places));
        if (places.isEmpty()) {
            logger.info("The place with address: " + address + " doesn't exist");
            return Optional.empty();
        }
        return placeDTOList;
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
    public Optional<List<PlaceDTO>> findOpenPlace(LocalTime time) {
        List<Place> places = placeRepository.findAll();
        LocalTime beforeMidnight = LocalTime.parse("23:59");
        LocalTime midnight = LocalTime.MIDNIGHT;
        for (Place place : places) {
            if(place.getOpening().isBefore(time) && place.getClosing().isAfter(time)) {
                List<Place> openPlace = placeRepository.isOpen(time, beforeMidnight, midnight);
                List<PlaceDTO> openPlaceDTO = listOfPlacesDTO(openPlace);
                return Optional.of(openPlaceDTO);
            }
        }
        logger.info("There are no open place at: " + time);
        return Optional.empty();
    }

    // method to find a list of places based on a current time
    public Optional<List<PlaceDTO>> findOpenPlaceNow() {
        List<Place> places = placeRepository.findAll();
        LocalTime now = LocalTime.now();
        LocalTime beforeMidnight = LocalTime.parse("23:59");
        LocalTime midnight = LocalTime.MIDNIGHT;
        for (Place place : places) {
            if (place.getOpening().isBefore(now) && place.getClosing().isAfter(now)) {
                List<Place> openPlace = placeRepository.isOpen(now, beforeMidnight, midnight);
                List<PlaceDTO> openPlaceDTO = listOfPlacesDTO(openPlace);
                return Optional.of(openPlaceDTO);
            }
        }
        logger.info("There are no open place now");
        return Optional.empty();
    }
}
