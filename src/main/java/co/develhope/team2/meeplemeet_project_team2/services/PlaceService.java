package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Place> getListPlaceType(String placeType) {
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

    //todo: considerare anche caso INACTIVE
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
        List<Place> places = placeRepository.findByAddress(address);
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
            Place existingPlace = placeOptional.get();
            if (placeOptional.get().getRecordStatusPlace() == RecordStatus.ACTIVE) {
                if(updatePlace.getAddress() != null) {
                    existingPlace.setAddress(updatePlace.getAddress());
                }
                if(updatePlace.getPlaceType() != null) {
                    existingPlace.setPlaceType(updatePlace.getPlaceType());
                }
                existingPlace.setInfo(updatePlace.getInfo());
                existingPlace.setName(updatePlace.getName());
                existingPlace.setMaxCapacity(updatePlace.getMaxCapacity());
                existingPlace.setOpening(updatePlace.getOpening());
                existingPlace.setClosing(updatePlace.getClosing());

                placeRepository.save(existingPlace);
                return existingPlace;
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

    //todo: riguardare questi due ultimi metodi
    public List<Place> findOpenPlace(LocalTime time) {
        for (Place place : placeRepository.findAll()) {
            if (place.getOpening().isBefore(time) && place.getClosing().isAfter(time)) {
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
