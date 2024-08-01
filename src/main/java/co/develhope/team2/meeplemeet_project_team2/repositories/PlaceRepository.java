package co.develhope.team2.meeplemeet_project_team2.repositories;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    @Query("SELECT place FROM Place place WHERE :currentTime BETWEEN place.opening AND place.closing")
    List<Place> isOpen(@Param("currentTime") LocalTime currentTime);
}