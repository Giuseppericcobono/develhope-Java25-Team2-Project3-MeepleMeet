package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(Review review){
        return reviewRepository.save(review);
    }

}