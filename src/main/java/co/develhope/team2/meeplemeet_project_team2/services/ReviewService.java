package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.ReviewDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.Rating;
import co.develhope.team2.meeplemeet_project_team2.repositories.ReviewRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private ReviewDTO createReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUsername(review.getUser().getUsername());
        reviewDTO.setDescription(review.getDescription());
        reviewDTO.setRating(review.getRating().getStars());
        return reviewDTO;
    }

    private List<ReviewDTO> createDTOListReview(List<Review> reviews) {
        List<ReviewDTO> reviewDTOList = new ArrayList<>();
        for(Review r : reviews) {
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setUsername(r.getUser().getUsername());
            reviewDTO.setDescription(r.getDescription());
            reviewDTO.setRating(r.getRating().getStars());
            reviewDTOList.add(reviewDTO);
        }
        return reviewDTOList;
    }

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

    public Optional<ReviewDTO> getReviewById(Integer id) {
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isPresent()) {
            ReviewDTO reviewDTO = createReviewDTO(review.get());
            return Optional.of(reviewDTO);
        } else {
            logger.info("Review with id: " + id + " not found");
            return Optional.empty();
        }
    }

    public Optional<List<ReviewDTO>> getAllReviewOfAUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            List<Review> reviewList = optionalUser.get().getReviews();
            List<ReviewDTO> reviewDTOList = createDTOListReview(reviewList);
            return Optional.of(reviewDTOList);
        } else {
            logger.info("User with id: " + id + " not found");
            return Optional.empty();
        }
    }

    public Optional<List<ReviewDTO>> getAllReviewOfAUserByUsername(String username) {
        List<Review> reviews = reviewRepository.findReviewsByUsername(username);
        List<ReviewDTO> reviewDTOList = createDTOListReview(reviews);
        if (reviews.isEmpty()) {
            logger.info("Username " + username + "doesn't exist");
            return Optional.empty();
        }
        return Optional.of(reviewDTOList);
    }

    public Optional<List<ReviewDTO>> getAllReviews(){
        List<Review> reviewList = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOList = createDTOListReview(reviewList);
        if(reviewDTOList.isEmpty()){
            logger.info("no reviews");
            return Optional.empty();
        }
        return Optional.of(reviewDTOList);
    }

    public Optional<Review> updateReview (Integer id, Review updateReview){
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if(optionalReview.isPresent()) {
            Review existingReview = optionalReview.get();
            existingReview.setDescription(updateReview.getDescription());
            existingReview.setRating(updateReview.getRating());

            reviewRepository.save(existingReview);
            existingReview.getUser().setStarRating(averageStarRating(existingReview.getUser().getUserId())); // update the StarRating of the selected user
            userRepository.save(existingReview.getUser());
            return Optional.of(existingReview);
        }
        logger.info("Review with id: " + id + " not found");
        return Optional.empty();
    }

    public void deleteReviewById (Integer id) {
        reviewRepository.deleteById(id);
    }

    public void deleteAllReview () {
        reviewRepository.deleteAll();
    }
}