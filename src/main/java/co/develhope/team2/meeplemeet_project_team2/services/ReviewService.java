package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.Rating;
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
        user.setStarRating(averageStarRating(user.getUserId()));
        userRepository.save(user);
        return review1;
    }

    // calculates average rating of a user
    public String averageStarRating(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User user1 = user.get();
            Double sumRating = 0.0;
            List<Review> reviews = reviewRepository.reviewsOfOneUser(user1);
            for (Review review : reviews) {
                sumRating += review.getRating().getValue();
            }

            if (!reviews.isEmpty()) {
                sumRating /= reviews.size();
            } else {
                return "No ratings found";
            }

            String starRating = Rating.STAR0.getStars();
            for (Rating rating : Rating.values()) {
                if (rating.getValue().equals(sumRating)) {
                    starRating = rating.getStars();
                } else if (sumRating <= 0.5) {
                    starRating = Rating.STAR0.getStars();
                } else if (sumRating > 0.5 && sumRating <= 1.5) {
                    starRating = Rating.STAR1.getStars();
                } else if (sumRating > 1.5 && sumRating <= 2.5) {
                    starRating = Rating.STAR2.getStars();
                } else if (sumRating > 2.5 && sumRating <= 3.5) {
                    starRating = Rating.STAR3.getStars();
                } else if (sumRating > 3.5 && sumRating <= 4.5) {
                    starRating = Rating.STAR4.getStars();
                } else if (sumRating > 4.5) {
                    starRating = Rating.STAR5.getStars();
                }
            }
            return starRating;
        } else {
            return null;
        }
    }

    //TODO eliminare
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