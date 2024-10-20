package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.PaginatedResponse;
import com.asdc.dalexchange.dto.ProductListingDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.service.ProductListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductListingControllerTest {
    @InjectMocks
    private ProductListingController productListingController;

    @Mock
    private ProductListingService productListingService;

    @Mock
    private Mapper<Product, ProductListingDTO> productListingMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProductListing() {
        Product product = new Product();
        ProductListingDTO productListingDTO = new ProductListingDTO();
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products);
        List<ProductListingDTO> productListingDTOs = List.of(productListingDTO);
        Page<ProductListingDTO> productListingDTOPage = new PageImpl<>(productListingDTOs);

        Pageable pageable = PageRequest.of(0, 10);

        when(productListingService.findByCriteria(eq(pageable), any(), any(), any(), any(), any())).thenReturn(productListingDTOPage);

        PaginatedResponse<ProductListingDTO> result = productListingController.getProductListing(0, 10, null, null, null, null, null);

        assertEquals(productListingDTOs, result.getContent());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        verify(productListingService).findByCriteria(eq(pageable), any(), any(), any(), any(), any());
    }
}