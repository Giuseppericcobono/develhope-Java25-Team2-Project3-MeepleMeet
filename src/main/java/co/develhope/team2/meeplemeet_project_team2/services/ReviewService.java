package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.repositories.ReviewRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    public Review createReview(Review review) {
        User user = userRepository.findById(review.getUser().getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Review review1 = new Review();
        review1.setUser(user);
        review1.setDescription(review.getDescription());
        review1.setRating(review.getRating());

        reviewRepository.save(review1);
        return review1;
    }

    public List<Review> getAllReviewOfAUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getReviews();
        } else {
            throw new EntityNotFoundException("User with id: " + id + " not found");
        }
    }

    public List<Review> getAllReviewOfAUserByUsername(String username) {
        List<Review> reviews = reviewRepository.findReviewsByUsername(username);
        if (reviews.isEmpty()) {
            throw new EntityNotFoundException("Username " + username + "doesn't exist");
        }
        return reviews;
    }

    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }

    public Review updateReview (Integer id, Review updateReview){
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if(optionalReview.isPresent()) {
            Review existingReview = optionalReview.get();
            existingReview.setDescription(updateReview.getDescription());
            existingReview.setRating(updateReview.getRating());

            reviewRepository.save(existingReview);
            return existingReview;
        }
        throw new EntityNotFoundException("Review with id: " + id + " not found");
    }

    public void deleteReviewById (Integer id) {
        reviewRepository.deleteById(id);
    }

    public void deleteAllReview () {
        reviewRepository.deleteAll();
    }
}