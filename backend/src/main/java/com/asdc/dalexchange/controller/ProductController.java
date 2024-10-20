package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.AddProductDTO;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductCategory;
import com.asdc.dalexchange.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for handling product-related requests.
 */
@RestController
@RequestMapping("/product")
@Slf4j
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    /**
     * Endpoint to add a new product.
     *
     * @param addProductDTO the DTO containing product details
     * @param imageFiles    the list of image files to be associated with the product
     * @return the saved product and HTTP status
     */
    @PostMapping("/add-product")
    public ResponseEntity<Product> addProduct(
            @RequestPart("addProductDTO") AddProductDTO addProductDTO,
            @RequestPart("files") List<MultipartFile> imageFiles) {

        log.info("Received request to add a product with details: {}", addProductDTO);

        // Retrieve the category of the product using the category ID from the DTO
        ProductCategory category = productService.getCategoryById(addProductDTO.getCategoryId());

        // Check if the category is valid; if not, return a bad request response
        if (category == null) {
            log.warn("Invalid category ID provided: {}", addProductDTO.getCategoryId());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // Add the product using the product service and save the product along with its images
        Product savedProduct;
        try {
            savedProduct = productService.addProduct(addProductDTO, category, imageFiles);
            log.info("Product successfully added with ID: {}", savedProduct.getProductId());
        } catch (Exception e) {
            log.error("Error occurred while adding the product", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return the saved product with a created status
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}
