    package com.vishwa.firstjobapp.review;

    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.List;
    import java.util.Optional;

    public interface ReviewRepository extends JpaRepository<Review, Long> {

        // This method finds all reviews associated with a specific company ID.
        // This was previously updated to correctly reference company.companyId
        List<Review> findByCompany_CompanyId(Long companyId);

        // FIX: Changed findByCompanyIdAndReviewId to findByCompany_CompanyIdAndReviewId
        // This explicitly tells Spring Data JPA to use the 'companyId' property
        // on the 'Company' entity when traversing the 'company' relationship,
        // and then combine it with the 'reviewId' property of the Review entity itself.
        Optional<Review> findByCompany_CompanyIdAndReviewId(Long companyId, Long reviewId);
    }
