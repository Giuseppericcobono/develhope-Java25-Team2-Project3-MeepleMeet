package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.DTO.ReviewDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.services.ReviewService;
import co.develhope.team2.meeplemeet_project_team2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public ResponseEntity<Review> newReview(@RequestBody Review review) {
        Review newReview = reviewService.createReview(review);
        return ResponseEntity.ok(newReview);
    }

    @GetMapping("/search/all")
    public ResponseEntity<Optional<List<ReviewDTO>>> allReviews() {
        Optional<List<ReviewDTO>> allReviews = reviewService.getAllReviews();
        if(allReviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allReviews);
    }

    @GetMapping("/search/byIDreview/{id}")
    public ResponseEntity<Optional<ReviewDTO>> getReviewById(@PathVariable Integer id) {
        Optional<ReviewDTO> reviewDTO = reviewService.getReviewById(id);
        if (reviewDTO.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reviewDTO);
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<Optional<List<ReviewDTO>>> reviewsUser(@PathVariable Integer userId) {
        Optional<List<ReviewDTO>> reviewsUser = reviewService.getAllReviewOfAUserById(userId);
        if (reviewsUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewsUser);
    }

    @GetMapping("/search")
    public ResponseEntity<Optional<List<ReviewDTO>>> reviewsUsername(@RequestParam(name = "username") String username) {
        Optional<List<ReviewDTO>> reviews = reviewService.getAllReviewOfAUserByUsername(username);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<Review>> updateAReview(@PathVariable Integer id, @RequestBody Review updateReview) {
        Optional<ReviewDTO> existingReview = reviewService.getReviewById(id);
        if (existingReview.isPresent()) {
            Optional<Review> review = reviewService.updateReview(id, updateReview);
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Optional<Review>> deleteReview(@PathVariable Integer id) {
        Optional<ReviewDTO> existingReview = reviewService.getReviewById(id);
        if (existingReview.isPresent()) {
            reviewService.deleteReviewById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Review> deleteAll() {
        reviewService.deleteAllReview();
        return ResponseEntity.ok().build();
    }
}
