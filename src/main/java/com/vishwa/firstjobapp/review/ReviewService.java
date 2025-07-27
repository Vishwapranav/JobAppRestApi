package com.vishwa.firstjobapp.review;

import java.util.List;

public interface ReviewService {

    List<Review> findAll(Long companyId);

    boolean addReview(Long companyId, Review review);

    // Parameter uses reviewId
    Review findById(Long companyId, Long reviewId);

    // Parameters use reviewId
    boolean updateReview(Long companyId, Long reviewId, Review review);

    // Parameters use reviewId
    boolean deleteReview(Long companyId, Long reviewId);
}
