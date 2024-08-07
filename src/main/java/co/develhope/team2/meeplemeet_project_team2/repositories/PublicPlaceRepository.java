package co.develhope.team2.meeplemeet_project_team2.repositories;

import co.develhope.team2.meeplemeet_project_team2.DTO.PlaceDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.PublicPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface PublicPlaceRepository extends JpaRepository<PublicPlace, Integer> {

    @Query("SELECT place FROM PublicPlace place WHERE place.name = :name")
    List<PlaceDTO> findByName(@Param("name")String name);

    @Query("SELECT place FROM PublicPlace place WHERE :time BETWEEN place.opening AND place.closing")
    List<PublicPlace> isOpen(@Param("time") LocalTime time);
}
