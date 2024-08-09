package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.PlaceDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.PublicPlace;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.PlaceRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.PublicPlaceRepository;
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

    @Autowired
    private PublicPlaceRepository publicPlaceRepository;

    @Transactional
    public Place createAPlace(Place place) {
        if(place.getPlaceType() == PlaceType.PUBLIC) {
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

    public List<Place> getListOfPlaces() {
       return placeRepository.findAll();
    }

    public List<Place> getListOfActivePlaces() {
        return placeRepository.statusEntity(RecordStatus.ACTIVE);
    }

    public List<Place> getListOfDeletedPlaces() {
        return placeRepository.statusEntity(RecordStatus.DELETED);
    }

    public List<Place> getListOfPublicPlace() {
        return placeRepository.findPlaceType(PlaceType.PUBLIC);
    }

    public List<Place> getListOfPrivatePlace() {
        return placeRepository.findPlaceType(PlaceType.PRIVATE);
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

    public List<PlaceDTO> getPlaceByName(String name) {
        List<PlaceDTO> places = publicPlaceRepository.findByName(name);
        List<PlaceDTO> placesOpen = places.stream().filter(place -> place.getRecordStatus() == RecordStatus.ACTIVE).toList();
        return placesOpen;
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

    public List<PublicPlace> findOpenPlace (LocalTime time) {
        for (PublicPlace place : publicPlaceRepository.findAll()) {
            if(place.getOpening().isBefore(time) && place.getClosing().isAfter(time)) {
                return publicPlaceRepository.isOpen(time);
            }
        }
        return null;
    }

    public List<PublicPlace> findOpenPlaceNow() {
        LocalTime now = LocalTime.now();
        for (PublicPlace place : publicPlaceRepository.findAll()) {
            if (place.getOpening().isBefore(now) && place.getClosing().isAfter(now)) {
                return publicPlaceRepository.isOpen(now);
            }
        }
        return null;
    }
}
