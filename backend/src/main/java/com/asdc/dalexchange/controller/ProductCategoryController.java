package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.model.ProductCategory;
import com.asdc.dalexchange.service.ProductCategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing product categories.
 */
@RestController
@Slf4j
@AllArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    /**
     * Retrieve all product categories.
     *
     * @return a list of product category names.
     */
    @GetMapping(path = "/product_categories")
    public List<String> getProductCategories() {
        log.info("get product_categories API endpoint called.");
        List<String> categories = productCategoryService.findAll();
        log.debug("Fetched {} product categories.", categories.size());
        return categories;
    }

    /**
     * Retrieve all product categories with details.
     *
     * @return a list of ProductCategory objects.
     */
    @GetMapping(path = "/categories")
    public List<ProductCategory> getCategories() {
        log.info("get categories API endpoint called.");
        List<ProductCategory> categories = productCategoryService.findAllCategories();
        log.debug("Fetched {} categories.", categories.size());
        return categories;
    }
}
