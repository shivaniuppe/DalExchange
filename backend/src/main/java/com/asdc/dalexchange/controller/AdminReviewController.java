package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.ProductRatingAdminDTO;
import com.asdc.dalexchange.service.ProductRatingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Controller class to handle admin review related requests.
 */
@RestController
@RequestMapping("/admin/reviews")
@Slf4j
@AllArgsConstructor
public class AdminReviewController {

    private ProductRatingService productRatingService;

    /**
     * Retrieves all reviews.
     *
     * @return List of ProductRatingAdminDTO containing all reviews.
     */
    @GetMapping("/all")
    public List<ProductRatingAdminDTO> getAllReviews() {
        log.info("Fetching all reviews");
        List<ProductRatingAdminDTO> reviews = productRatingService.getAllReviews();
        log.info("All reviews fetched successfully");
        return reviews;
    }

    /**
     * Retrieves all reviews for a specific product by its ID.
     *
     * @param productId the ID of the product to retrieve reviews for.
     * @return List of ProductRatingAdminDTO containing reviews for the specified product.
     */
    @GetMapping("/product/{productId}")
    public List<ProductRatingAdminDTO> getAllReviewsByProduct(@PathVariable Long productId) {
        log.info("Fetching reviews for product ID: {}", productId);
        List<ProductRatingAdminDTO> reviews = productRatingService.getAllReviewsByProduct(productId);
        log.info("Reviews for product ID: {} fetched successfully", productId);
        return reviews;
    }

    /**
     * Deletes a review by product ID and user ID.
     *
     * @param productId the ID of the product associated with the review.
     * @param userId the ID of the user who submitted the review.
     */
    @DeleteMapping("/delete")
    public void deleteReview(@RequestParam Long productId, @RequestParam Long userId) {
        log.info("Deleting review for product ID: {} and user ID: {}", productId, userId);
        productRatingService.deleteReview(productId, userId);
        log.info("Review for product ID: {} and user ID: {} deleted successfully", productId, userId);
    }
}
