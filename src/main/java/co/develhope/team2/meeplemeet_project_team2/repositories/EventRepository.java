package co.develhope.team2.meeplemeet_project_team2.repositories;

import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.EventStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e WHERE e.eventStatusEnum = :status")
    List<Event> findEventsByStatus(@Param("status") EventStatusEnum status);

    // Recupera solo gli eventi non eliminati
    @Query("SELECT e FROM Event e WHERE e.isDeleted = false")
    List<Event> findAllNotDeleted();

    // Recupera un singolo evento non eliminato
    @Query("SELECT e FROM Event e WHERE e.id = :id AND e.isDeleted = false")
    Event findByIdAndNotDeleted(Integer id);

    // Recupera solo gli eventi non eliminati
    @Query("SELECT e FROM Event e WHERE e.isDeleted = true")
    List<Event> findAllDeleted();

    // Recupera un singolo evento non eliminato
    @Query("SELECT e FROM Event e WHERE e.id = :id AND e.isDeleted = true")
    Event findByIdAndDeleted(Integer id);

    // Ricerca eventi per nome dell'evento
    @Query("SELECT e FROM Event e WHERE e.name LIKE %:name% AND e.isDeleted = false")
    List<Event> findEventsByEventNameContaining(@Param("name") String name);

    @Query("SELECT e FROM Event e WHERE e.nameGame LIKE %:gameName% AND e.isDeleted = false")
    List<Event> findEventsByGameNameContaining(@Param("gameName") String gameName);
}