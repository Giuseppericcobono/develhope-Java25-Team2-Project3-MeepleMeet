package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.services.ReviewService;
import co.develhope.team2.meeplemeet_project_team2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public ResponseEntity<Review> newReview(@RequestBody Review review){
        Review newReview = reviewService.createReview(review);
        return ResponseEntity.ok(newReview);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<Review>> allReviews(){
        List<Review> allReviews = reviewService.getAllReviews();
        return ResponseEntity.ok(allReviews);
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<List<Review>> reviewsUser(@PathVariable Integer userId){
        List<Review> reviewsUser = reviewService.getAllReviewOfAUserById(userId);
        return ResponseEntity.ok(reviewsUser);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Review>> reviewsUsername(@RequestParam(name = "username") String username){
        List<Review> reviews = reviewService.getAllReviewOfAUserByUsername(username);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Review> updateAReview(@PathVariable Integer id, @RequestBody Review updateReview){
        Review review = reviewService.updateReview(id, updateReview);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Review> deleteReview(@PathVariable Integer id){
        reviewService.deleteReviewById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Review> deleteAll(){
        reviewService.deleteAllReview();
        return ResponseEntity.ok().build();
    }
}
