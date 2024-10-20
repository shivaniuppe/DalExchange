package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.ProductDetailsDTO;
import com.asdc.dalexchange.dto.ProductWishlistDTO;
import com.asdc.dalexchange.service.ProductDetailsService;
import com.asdc.dalexchange.service.ProductWishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling product-details.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/product_details")
@Slf4j
public class ProductDetailsController {

    private final ProductDetailsService productDetailsService;
    private final ProductWishlistService productWishlistService;

    /**
     * Retrieves the details of a product based on the provided product ID.
     *
     * @param productId the ID of the product to retrieve details for.
     * @return a {@link ResponseEntity} containing the {@link ProductDetailsDTO} with product details.
     */
    @GetMapping("")
    public ResponseEntity<ProductDetailsDTO> product(
            @RequestParam(defaultValue = "0") Long productId) {
        log.info("Fetching product details for productId: {}", productId);
        ProductDetailsDTO productDetailsDTO = productDetailsService.getDetails(productId);
        log.info("Fetched product details successfully for productId: {}", productId);
        return ResponseEntity.ok().body(productDetailsDTO);
    }

    /**
     * Marks a product as a favorite and adds it to the user's wishlist.
     *
     * @param productId the ID of the product to be marked as a favorite.
     * @return a {@link ResponseEntity} with a message indicating that the product was successfully added to the wishlist.
     */
    @GetMapping("/favorite")
    public ResponseEntity<String> markAsFavorite(
            @RequestParam(defaultValue = "0") Long productId) {
        log.info("Marking product as favorite for productId: {}", productId);
        ProductWishlistDTO productWishlistDTO = new ProductWishlistDTO();
        productWishlistDTO.setProductId(productId);

        productWishlistService.markProductAsFavorite(productId);

        log.info("Product added successfully to wishlist for productId: {}", productId);
        return ResponseEntity.ok().body("Product added Successfully in wishlist.");
    }
}
