package co.develhope.team2.meeplemeet_project_team2.repositories;


import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    @Query("SELECT place FROM Place place WHERE place.name = :name AND place.recordStatusPlace = 'ACTIVE'")
    List<Place> findByName(@Param("name") String name);

    @Query("SELECT place FROM Place place WHERE place.recordStatusPlace = :status")
    List<Place> statusEntity(@Param("status")RecordStatus recordStatus);

    @Query("SELECT place FROM Place place WHERE place.address = :address AND place.recordStatusPlace = 'ACTIVE'")
    List<Place> findByAddress(@Param("address")String address);

    @Query("SELECT place FROM Place place WHERE place.placeType = :placeType AND place.recordStatusPlace = 'ACTIVE'")
    List<Place> findPlaceType(@Param("placeType")PlaceType placeType);

    @Query("SELECT place FROM Place place WHERE :time BETWEEN place.opening AND place.closing AND place.recordStatusPlace = 'ACTIVE'")
    List<Place> isOpen(@Param("time") LocalTime time);
}