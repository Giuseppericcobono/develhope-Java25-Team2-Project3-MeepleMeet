package co.develhope.team2.meeplemeet_project_team2.repositories;


import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    @Query("SELECT place FROM Place place WHERE place.recordStatusPlace = :status")
    List<Place> statusEntity(@Param("status")RecordStatus recordStatus);

    @Query("SELECT place FROM Place place WHERE place.address = :address")
    Optional<Place> findByAdress(@Param("address")String address);

    @Query("SELECT place FROM Place place WHERE place.placeType = :placeType")
    List<Place> findPlaceType(@Param("placeType")PlaceType placeType);

}