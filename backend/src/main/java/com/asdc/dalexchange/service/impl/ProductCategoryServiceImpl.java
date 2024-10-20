package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.model.ProductCategory;
import com.asdc.dalexchange.repository.ProductCategoryRepository;
import com.asdc.dalexchange.service.ProductCategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the ProductCategoryService interface for managing product categories.
 */
@Service
@Slf4j
@AllArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    /**
     * Finds all product category names.
     *
     * @return a list of product category names.
     */
    @Override
    public List<String> findAll() {
        log.info("Find all call started in the ProductCategoryServiceImpl");
        List<String> categoryNames = productCategoryRepository.findAll()
                .stream()
                .map(ProductCategory::getName)
                .toList();
        log.debug("Found {} product category names", categoryNames.size());
        return categoryNames;
    }

    /**
     * Finds all product categories.
     *
     * @return a list of ProductCategory objects.
     */
    @Override
    public List<ProductCategory> findAllCategories() {
        log.info("Find all categories call started in the ProductCategoryServiceImpl");
        List<ProductCategory> categories = productCategoryRepository.findAll();
        log.debug("Found {} categories", categories.size());
        return categories;
    }

}
