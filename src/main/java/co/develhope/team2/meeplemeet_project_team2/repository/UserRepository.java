package co.develhope.team2.meeplemeet_project_team2.repository;

import co.develhope.team2.meeplemeet_project_team2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
