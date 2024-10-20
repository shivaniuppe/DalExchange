package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.ProductModerationDTO;
import com.asdc.dalexchange.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Controller class to handle admin product related requests.
 */
@RestController
@RequestMapping("/admin/products")
@Slf4j
@AllArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    /**
     * Retrieves all products.
     *
     * @return List of ProductModerationDTO containing product details.
     */
    @GetMapping
    public List<ProductModerationDTO> getAllProducts() {
        log.info("Fetching all products");
        List<ProductModerationDTO> products = productService.getAllProducts();
        log.info("All products fetched successfully");
        return products;
    }

    /**
     * Retrieves product details by ID.
     *
     * @param productId the ID of the product to retrieve.
     * @return ProductModerationDTO containing product details.
     */
    @GetMapping("/{productId}")
    public ProductModerationDTO getProductById(@PathVariable Long productId) {
        log.info("Fetching product details for product ID: {}", productId);
        ProductModerationDTO product = productService.getProductByIdForModeration(productId);
        log.info("Product details fetched successfully for product ID: {}", productId);
        return product;
    }

    /**
     * Updates a product.
     *
     * @param productId the ID of the product to update.
     * @param updatedProductDetails the updated product details.
     * @return ProductModerationDTO containing updated product details.
     */
    @PutMapping("/update/{productId}")
    public ProductModerationDTO updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductModerationDTO updatedProductDetails) {
        log.info("Updating product ID: {}", productId);
        ProductModerationDTO updatedProduct = productService.updateProduct(productId, updatedProductDetails);
        log.info("Product ID: {} updated successfully", productId);
        return updatedProduct;
    }

    /**
     * Unlists or lists a product.
     *
     * @param productId the ID of the product to unlist or list.
     * @param unlisted boolean value to set the product as unlisted or listed.
     */
    @PutMapping("/unlist/{productId}")
    public void unlistProduct(@PathVariable Long productId, @RequestParam boolean unlisted) {
        log.info("Setting unlisted status for product ID: {} to {}", productId, unlisted);
        productService.unlistProduct(productId, unlisted);
        log.info("Unlisted status for product ID: {} set to {}", productId, unlisted);
    }
}
