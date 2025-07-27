package com.vishwa.firstjobapp.review;

import com.vishwa.firstjobapp.company.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies/{companyId}/reviews")
public class ReviewController {

    private ReviewService reviewService;

    private CompanyService companyService; // Assuming this is still needed for some context, though not directly used in provided methods

    public ReviewController(ReviewService reviewService, CompanyService companyService) {
        this.reviewService = reviewService;
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.findAll(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createReview(@PathVariable Long companyId, @RequestBody Review review){
        if (reviewService.addReview(companyId, review)){
            return new ResponseEntity<>("Review added successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not added", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{reviewId}") // Path variable uses reviewId
    public ResponseEntity<Review> getReviewById(@PathVariable Long companyId, @PathVariable Long reviewId){ // Parameter uses reviewId
        // The service call is correct here, it uses the reviewId parameter
        return new ResponseEntity<>(reviewService.findById(companyId, reviewId), HttpStatus.OK);
    }

    @PutMapping("/{reviewId}") // Path variable uses reviewId
    public ResponseEntity<String> updateReview(@PathVariable Long companyId,
                                               @PathVariable Long reviewId,
                                               @RequestBody Review review){
        // Parameters use reviewId
        if (reviewService.updateReview(companyId, reviewId, review))
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        return new ResponseEntity<>("Review not updated", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reviewId}") // Path variable uses reviewId
    public ResponseEntity<String> deleteReview(@PathVariable Long companyId, @PathVariable Long reviewId){ // Parameters use reviewId
        if (reviewService.deleteReview(companyId, reviewId)){
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not deleted", HttpStatus.NOT_FOUND);
    }
}
