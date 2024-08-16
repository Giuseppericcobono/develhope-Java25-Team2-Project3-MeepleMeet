package co.develhope.team2.meeplemeet_project_team2.repositories;

import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT review FROM Review review WHERE review.user = :user")
    List<Review> reviewsOfOneUser(@Param("user") User user);
}