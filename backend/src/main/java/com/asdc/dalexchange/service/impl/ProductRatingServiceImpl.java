package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.ProductRatingAdminDTO;
import com.asdc.dalexchange.dto.ProductRatingDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductRating;
import com.asdc.dalexchange.model.ProductRatingID;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.ProductRatingRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.service.ProductRatingService;
import com.asdc.dalexchange.specifications.ProductSpecification;
import com.asdc.dalexchange.util.AuthUtil;
import com.asdc.dalexchange.util.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ProductRatingService} interface for managing product ratings.
 * Provides functionality for saving, deleting, and retrieving product reviews.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRatingServiceImpl implements ProductRatingService {

    private final ProductRatingRepository productRatingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final Mapper<ProductRating, ProductRatingDTO> productRatingMapper;
    private final Mapper<ProductRating, ProductRatingAdminDTO> productRatingAdminMapper;

    /**
     * Retrieves all product reviews for all sold items of the current user.
     *
     * @return a list of {@link ProductRatingDTO} representing the reviews for all sold items of the current user.
     */
    @Override
    public List<ProductRatingDTO> allReviewOfAllSoldItemsOfUser() {
        Long userId = AuthUtil.getCurrentUserId(userRepository);
        log.info("Fetching all reviews for sold items of userId: {}", userId);

        List<Product> products = productRepository.findAll(ProductSpecification.bySellerUserId(userId));
        List<Long> productIds = products.stream()
                .map(Product::getProductId)
                .collect(Collectors.toList());

        List<ProductRating> allProductRatings = productIds.stream()
                .flatMap(productId -> productRatingRepository.findByIdProductId(productId).stream())
                .collect(Collectors.toList());

        List<ProductRatingDTO> productRatingDTOs = allProductRatings.stream()
                .map(productRatingMapper::mapTo)
                .collect(Collectors.toList());

        log.info("Fetched {} product ratings for userId: {}", productRatingDTOs.size(), userId);
        return productRatingDTOs;
    }

    /**
     * Retrieves all product ratings for administrative purposes.
     *
     * @return a list of {@link ProductRatingAdminDTO} representing all product reviews.
     */
    @Override
    public List<ProductRatingAdminDTO> getAllReviews() {
        log.info("Fetching all product ratings");

        List<ProductRatingAdminDTO> allReviews = productRatingRepository.findAll().stream()
                .map(productRatingAdminMapper::mapTo)
                .collect(Collectors.toList());

        log.info("Fetched {} product ratings", allReviews.size());
        return allReviews;
    }

    /**
     * Retrieves all product reviews for a specific product.
     *
     * @param productId the ID of the product whose reviews are to be fetched.
     * @return a list of {@link ProductRatingAdminDTO} representing the reviews for the specified product.
     */
    @Override
    public List<ProductRatingAdminDTO> getAllReviewsByProduct(Long productId) {
        log.info("Fetching all reviews for productId: {}", productId);

        List<ProductRatingAdminDTO> reviewsByProduct = productRatingRepository.findByIdProductId(productId).stream()
                .map(productRatingAdminMapper::mapTo)
                .collect(Collectors.toList());

        log.info("Fetched {} reviews for productId: {}", reviewsByProduct.size(), productId);
        return reviewsByProduct;
    }

    /**
     * Deletes a product review for a specific product and user.
     *
     * @param productId the ID of the product whose review is to be deleted.
     * @param userId the ID of the user whose review is to be deleted.
     */
    @Transactional
    @Override
    public void deleteReview(Long productId, Long userId) {
        ProductRatingID id = new ProductRatingID(productId, userId);
        log.info("Deleting review for productId: {} and userId: {}", productId, userId);

        productRatingRepository.deleteById(id);
        log.info("Deleted review with productId: {} and userId: {}", productId, userId);
    }

    /**
     * Saves a product rating and review provided in the request body.
     *
     * @param requestBody a map containing the product ID, user ID, rating, and review.
     * @throws ResourceNotFoundException if the user or product is not found.
     */
    @Override
    public void saveRating(Map<String, Object> requestBody) {
        Long productId = Long.parseLong(requestBody.get("productId").toString());
        Long userId = AuthUtil.getCurrentUserId(userRepository);
        Integer rating = Integer.parseInt(requestBody.get("rating").toString());
        String review = requestBody.get("review").toString();

        log.info("Saving rating for productId: {} by userId: {}", productId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new ResourceNotFoundException("User not found with id " + userId);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", productId);
                    return new ResourceNotFoundException("Product not found with id " + productId);
                });

        ProductRating productRating = new ProductRating();
        productRating.setRating(rating);
        productRating.setReview(review);
        productRating.setProduct(product);
        productRating.setUser(user);
        ProductRatingID productRatingID = new ProductRatingID(productId, userId);
        productRating.setId(productRatingID);
        productRatingRepository.save(productRating);
        log.info("Successfully saved rating for productId: {} by userId: {}", productId, userId);
    }
}
