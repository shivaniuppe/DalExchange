package com.asdc.dalexchange.service;

import com.asdc.dalexchange.model.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<String> findAll();
    List<ProductCategory> findAllCategories();
}
