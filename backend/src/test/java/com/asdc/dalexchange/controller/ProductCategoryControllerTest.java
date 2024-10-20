package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.service.ProductCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductCategoryControllerTest {
    @InjectMocks
    private ProductCategoryController productCategoryController;

    @Mock
    private ProductCategoryService productCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProductCategories() {
        List<String> categories = List.of("Electronics", "Books", "Clothing");

        when(productCategoryService.findAll()).thenReturn(categories);

        List<String> result = productCategoryController.getProductCategories();

        assertEquals(categories, result);
        verify(productCategoryService).findAll();
    }
}