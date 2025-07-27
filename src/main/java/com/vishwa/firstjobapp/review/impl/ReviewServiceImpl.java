package com.vishwa.firstjobapp.review.impl;

import com.vishwa.firstjobapp.company.Company;
import com.vishwa.firstjobapp.company.CompanyService;
import com.vishwa.firstjobapp.review.Review;
import com.vishwa.firstjobapp.review.ReviewRepository;
import com.vishwa.firstjobapp.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // Import Optional for better handling of findById results

@Service
public class ReviewServiceImpl implements ReviewService {

    // Inject ReviewRepository for database operations on Review entities
    private final ReviewRepository reviewRepository;

    // Inject CompanyService to fetch Company entities, crucial for linking reviews
    private final CompanyService companyService;

    // Constructor for Dependency Injection: Spring automatically provides instances
    // of ReviewRepository and CompanyService when creating ReviewServiceImpl
    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService = companyService;
    }

    /**
     * Retrieves all reviews associated with a specific company.
     * Leverages Spring Data JPA's derived query method `findByCompany_CompanyId`.
     *
     * @param companyId The ID of the company whose reviews are to be retrieved.
     * @return A list of Review objects belonging to the specified company.
     */
    @Override
    public List<Review> findAll(Long companyId) {
        // This method call is correct based on the ReviewRepository Canvas
        return reviewRepository.findByCompany_CompanyId(companyId);
    }

    /**
     * Adds a new review to a specified company.
     * This method ensures the review is correctly linked to the company identified by companyId
     * from the URL path, overriding any potentially conflicting company ID in the request body.
     *
     * @param companyId The ID of the company to which the review will be added (from URL path).
     * @param review The Review object containing the new review's details (from request body).
     * @return true if the review was successfully added, false if the company does not exist.
     */
    @Override
    public boolean addReview(Long companyId, Review review) {
        // 1. Fetch the Company entity using the companyId from the path variable.
        // This is the authoritative source for the parent company.
        Company company = companyService.getCompanyById(companyId);

        // 2. Check if the company exists. A review cannot be added to a non-existent company.
        if (company != null) {
            // 3. Associate the fetched Company object with the Review object.
            // This is crucial: it explicitly sets the correct Company on the review,
            // overriding any company ID that might have been in the request body,
            // ensuring data integrity.
            review.setCompany(company);

            // 4. Save the review entity to the database.
            reviewRepository.save(review);
            return true; // Indicate success
        }
        // If company not found, return false
        return false;
    }

    /**
     * Finds a specific review by its ID within the context of a particular company.
     * This version directly queries the repository for a review matching both companyId and reviewId,
     * which is more efficient than fetching all reviews for a company and then filtering in memory.
     *
     * @param companyId The ID of the company.
     * @param reviewId The ID of the review to find.
     * @return The Review object if found, otherwise null.
     */
    @Override
    public Review findById(Long companyId, Long reviewId) {
        // FIX: Changed findByCompanyIdAndReviewId to findByCompany_CompanyIdAndReviewId
        // This now correctly matches the method signature in ReviewRepository.java
        return reviewRepository.findByCompany_CompanyIdAndReviewId(companyId, reviewId).orElse(null);
    }

    /**
     * Updates an existing review for a specific company.
     * It ensures the review belongs to the specified company before updating.
     *
     * @param companyId The ID of the company the review belongs to.
     * @param reviewId The ID of the review to update.
     * @param newReview The Review object containing the updated data.
     * @return true if the review was found and updated, false otherwise.
     */
    @Override
    public boolean updateReview(Long companyId, Long reviewId, Review newReview) {
        // FIX: Changed findByCompanyIdAndReviewId to findByCompany_CompanyIdAndReviewId
        Optional<Review> existingReviewOptional = reviewRepository.findByCompany_CompanyIdAndReviewId(companyId, reviewId);

        if (existingReviewOptional.isPresent()) {
            Review existingReview = existingReviewOptional.get();

            // 2. Update the fields of the existing review with data from newReview.
            // Note: The company relationship is generally not updated here, as a review
            // is typically fixed to its company.
            existingReview.setName(newReview.getName());
            existingReview.setDescription(newReview.getDescription());
            existingReview.setRating(newReview.getRating());

            // 3. Save the updated review. Since it's a managed entity, save() performs an UPDATE.
            reviewRepository.save(existingReview);
            return true; // Indicate successful update
        }
        return false; // Review not found for the given companyId and reviewId
    }

    /**
     * Deletes a specific review for a particular company.
     * It ensures the review belongs to the specified company before deleting.
     *
     * @param companyId The ID of the company the review belongs to.
     * @param reviewId The ID of the review to delete.
     * @return true if the review was found and deleted, false otherwise.
     */
    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {
        // FIX: Changed findByCompanyIdAndReviewId to findByCompany_CompanyIdAndReviewId
        Optional<Review> reviewToDeleteOptional = reviewRepository.findByCompany_CompanyIdAndReviewId(companyId, reviewId);

        if (reviewToDeleteOptional.isPresent()) {
            Review reviewToDelete = reviewToDeleteOptional.get();
            // 2. Delete the found review from the database.
            reviewRepository.delete(reviewToDelete);
            return true; // Indicate successful deletion
        }
        return false; // Review not found for the given companyId and reviewId
    }
}
