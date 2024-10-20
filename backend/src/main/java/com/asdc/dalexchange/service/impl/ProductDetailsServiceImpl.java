package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.ProductDetailsDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductImage;
import com.asdc.dalexchange.model.ProductWishlist;
import com.asdc.dalexchange.repository.ProductImageRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.ProductWishlistRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.service.ProductDetailsService;
import com.asdc.dalexchange.specifications.ProductImageSpecification;
import com.asdc.dalexchange.specifications.ProductWishlistSpecification;
import com.asdc.dalexchange.util.AuthUtil;
import com.asdc.dalexchange.util.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link ProductDetailsService} interface that provides methods
 * to retrieve and process details about products, including product images, favorite status,
 * and product information.
 */
@Service
@Slf4j
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductWishlistRepository productWishlistRepository;
    private final Mapper<Product, ProductDetailsDTO> productDetailsMapper;

    /**
     * Constructs a {@link ProductDetailsServiceImpl} with the specified repositories and mapper.
     *
     * @param productRepository         the repository for accessing product data.
     * @param userRepository            the repository for accessing user data.
     * @param productImageRepository    the repository for accessing product image data.
     * @param productWishlistRepository the repository for accessing product wishlist data.
     * @param productDetailsMapper      the mapper to convert between {@link Product} and {@link ProductDetailsDTO}.
     */
    public ProductDetailsServiceImpl(
            ProductRepository productRepository,
            UserRepository userRepository,
            ProductImageRepository productImageRepository,
            ProductWishlistRepository productWishlistRepository,
            Mapper<Product, ProductDetailsDTO> productDetailsMapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productImageRepository = productImageRepository;
        this.productWishlistRepository = productWishlistRepository;
        this.productDetailsMapper = productDetailsMapper;
    }

    /**
     * Fetches detailed information about a product, including images, category, seller details,
     * and favorite status.
     *
     * @param productId the ID of the product to retrieve details for.
     * @return a {@link ProductDetailsDTO} containing detailed information about the product.
     * @throws ResourceNotFoundException if the product with the specified ID is not found.
     */
    @Override
    public ProductDetailsDTO getDetails(Long productId) {
        log.info("Fetching details for productId: {}", productId);

        Long userId = AuthUtil.getCurrentUserId(userRepository);
        log.debug("Current userId: {}", userId);

        Product product = getProductById(productId);
        List<String> productImageUrls = getImageUrls(productId);

        ProductDetailsDTO productDetailsDTO = productDetailsMapper.mapTo(product);
        productDetailsDTO.setImageUrls(productImageUrls);
        productDetailsDTO.setCategory(product.getCategory().getName());
        productDetailsDTO.setSellerId(product.getSeller().getUserId());
        productDetailsDTO.setSellerJoiningDate(product.getSeller().getJoinedAt());

        Double sellerRating = product.getSeller().getSellerRating();
        double rating = (sellerRating != null) ? sellerRating.doubleValue() : 0.0;
        productDetailsDTO.setRating(rating);
        productDetailsDTO.setFavorite(getFavoriteStatus(userId, productId));

        log.info("Fetched product details successfully for productId: {}", productId);
        return productDetailsDTO;
    }

    /**
     * Retrieves a {@link Product} entity by its ID.
     *
     * @param productId the ID of the product to retrieve.
     * @return the {@link Product} entity with the specified ID.
     * @throws ResourceNotFoundException if the product with the specified ID is not found.
     */
    @Override
    public Product getProductById(Long productId) {
        log.info("Fetching product by ID: {}", productId);
        return this.productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new ResourceNotFoundException("Product not found with ID: " + productId);
                });
    }

    /**
     * Retrieves the URLs of images associated with a given product ID.
     *
     * @param productId the ID of the product to retrieve image URLs for.
     * @return a list of image URLs associated with the specified product.
     */
    @Override
    public List<String> getImageUrls(Long productId) {
        log.info("Fetching image URLs for productId: {}", productId);
        Specification<ProductImage> spec = ProductImageSpecification.byProductId(productId);
        List<ProductImage> productImages = productImageRepository.findAll(spec);
        List<String> imageUrls = productImages.stream()
                .map(ProductImage::getImageUrl)
                .toList();
        log.debug("Image URLs fetched: {}", imageUrls);
        return imageUrls;
    }

    /**
     * Checks whether a product is marked as a favorite by a user.
     *
     * @param userId    the ID of the user.
     * @param productId the ID of the product.
     * @return true if the product is marked as a favorite by the user; false otherwise.
     */
    @Override
    public boolean getFavoriteStatus(long userId, long productId) {
        log.info("Checking favorite status for userId: {} and productId: {}", userId, productId);
        Specification<ProductWishlist> spec = ProductWishlistSpecification.byUserIdAndProductId(userId, productId);
        long count = productWishlistRepository.count(spec);
        boolean isFavorite = count > 0;
        log.debug("Favorite status for userId: {} and productId: {} is {}", userId, productId, isFavorite);
        return isFavorite;
    }
}
