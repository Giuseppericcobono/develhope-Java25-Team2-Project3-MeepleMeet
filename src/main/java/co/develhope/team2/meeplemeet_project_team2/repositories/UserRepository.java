package co.develhope.team2.meeplemeet_project_team2.repositories;

import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT user FROM User user WHERE user.username = :username AND user.recordStatus = 'ACTIVE'")
    List<User> findUsersByUsername(@Param("username") String username);

    @Query("SELECT user FROM User user WHERE user.firstName = :firstName AND user.recordStatus = 'ACTIVE'")
    List<User> findUsersByFirstName(@Param("firstName") String firstName);

    @Query("SELECT user FROM User user WHERE user.lastName = :lastName AND user.recordStatus = 'ACTIVE'")
    List<User> findUsersByLastName(@Param("lastName") String lastName);

    @Query("SELECT user FROM User user WHERE user.firstName = :firstName AND user.lastName = :lastName AND user.recordStatus = 'ACTIVE'")
    List<User> findUsersByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT user FROM User user WHERE user.recordStatus = :status")
    List<User> recordStatusEntity(@Param("status") RecordStatus recordStatus);
}
