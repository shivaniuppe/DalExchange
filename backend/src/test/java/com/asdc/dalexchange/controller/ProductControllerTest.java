package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.AddProductDTO;
import com.asdc.dalexchange.enums.ProductCondition;
import com.asdc.dalexchange.enums.ShippingType;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductCategory;
import com.asdc.dalexchange.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProductTest() throws IOException {
        AddProductDTO addProductDTO = new AddProductDTO();
        addProductDTO.setTitle("Test Product");
        addProductDTO.setDescription("Test Description");
        addProductDTO.setPrice(100.0);
        addProductDTO.setProductCondition(ProductCondition.New);
        addProductDTO.setUseDuration("1 year");
        addProductDTO.setShippingType(ShippingType.Free);
        addProductDTO.setQuantityAvailable(10);
        addProductDTO.setCategoryId(1L);

        ProductCategory category = new ProductCategory();
        category.setCategoryId(1L);
        category.setName("Test Category");

        MultipartFile file1 = mock(MultipartFile.class);
        when(file1.getOriginalFilename()).thenReturn("test1.jpg");
        when(file1.isEmpty()).thenReturn(false);
        when(file1.getBytes()).thenReturn(new byte[0]);

        MultipartFile file2 = mock(MultipartFile.class);
        when(file2.getOriginalFilename()).thenReturn("test2.jpg");
        when(file2.isEmpty()).thenReturn(false);
        when(file2.getBytes()).thenReturn(new byte[0]);

        List<MultipartFile> files = Arrays.asList(file1, file2);

        Product savedProduct = new Product();
        savedProduct.setProductId(1L);
        savedProduct.setTitle(addProductDTO.getTitle());
        savedProduct.setDescription(addProductDTO.getDescription());
        savedProduct.setPrice(addProductDTO.getPrice());
        savedProduct.setCategory(category);
        savedProduct.setProductCondition(addProductDTO.getProductCondition());
        savedProduct.setUseDuration(addProductDTO.getUseDuration());
        savedProduct.setShippingType(addProductDTO.getShippingType());
        savedProduct.setQuantityAvailable(addProductDTO.getQuantityAvailable());
        savedProduct.setCreatedAt(LocalDateTime.now());

        when(productService.getCategoryById(1L)).thenReturn(category);
        when(productService.addProduct(any(AddProductDTO.class), any(ProductCategory.class), anyList())).thenReturn(savedProduct);

        ResponseEntity<Product> response = productController.addProduct(addProductDTO, files);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedProduct.getProductId(), response.getBody().getProductId());
        verify(productService, times(1)).getCategoryById(1L);
        verify(productService, times(1)).addProduct(any(AddProductDTO.class), any(ProductCategory.class), anyList());
    }

    @Test
    void addProductTest_CategoryNotFound() {
        AddProductDTO addProductDTO = new AddProductDTO();
        addProductDTO.setCategoryId(1L);

        MultipartFile file = mock(MultipartFile.class);
        List<MultipartFile> files = Collections.singletonList(file);

        when(productService.getCategoryById(1L)).thenReturn(null);

        ResponseEntity<Product> response = productController.addProduct(addProductDTO, files);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(productService, times(1)).getCategoryById(1L);
        verify(productService, times(0)).addProduct(any(AddProductDTO.class), any(ProductCategory.class), anyList());
    }
}
