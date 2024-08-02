package co.develhope.team2.meeplemeet_project_team2.repositories;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    @Query("SELECT place FROM Place place WHERE :time BETWEEN place.opening AND place.closing")
    List<Place> isOpen(@Param("time") LocalTime time);

    @Query("SELECT place FROM Place place WHERE place.recordStatusPlace = :status")
    List<Place> statusEntity(@Param("status")RecordStatus recordStatus);

    @Query("SELECT place FROM Place place WHERE place.name = :name")
    Optional<Place> findByName(@Param("name")String name);

    @Query("SELECT place FROM Place place WHERE place.address = :address")
    Optional<Place> findByAdress(@Param("address")String address);
}