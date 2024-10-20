package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.ProductListingDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductImage;
import com.asdc.dalexchange.repository.ProductImageRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.specifications.ProductSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductListingServiceImplTest {

    @InjectMocks
    private ProductListingServiceImpl productListingService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private Mapper<Product, ProductListingDTO> productListingMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByCriteriaWithCategory() {
        Product product = new Product();
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products);
        List<String> categories = List.of("Electronics");

        ProductListingDTO productListingDTO = new ProductListingDTO();
        ProductImage productImage = new ProductImage();
        productImage.setImageId(1L);
        productImage.setImageUrl("image-url");
        productImage.setProduct(product);
        List<ProductImage> productImages = List.of(productImage);

        Specification<Product> spec = ProductSpecification.hasCategory(categories);
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productListingMapper.mapTo(product)).thenReturn(productListingDTO);
        when(productImageRepository.findAll(any(Specification.class))).thenReturn(productImages);

        Page<ProductListingDTO> result = productListingService.findByCriteria(pageable, null, categories, null, null, null);

        assertEquals(1, result.getTotalElements());
        assertEquals("image-url", result.getContent().get(0).getImageUrl());
        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productImageRepository).findAll(any(Specification.class));
    }

    @Test
    public void testFindByCriteriaWithCondition() {
        Product product = new Product();
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products);
        List<String> conditions = List.of("New");

        ProductListingDTO productListingDTO = new ProductListingDTO();
        ProductImage productImage = new ProductImage();
        productImage.setImageId(1L);
        productImage.setImageUrl("image-url");
        productImage.setProduct(product);
        List<ProductImage> productImages = List.of(productImage);

        Specification<Product> spec = ProductSpecification.hasCondition(conditions);
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productListingMapper.mapTo(product)).thenReturn(productListingDTO);
        when(productImageRepository.findAll(any(Specification.class))).thenReturn(productImages);

        Page<ProductListingDTO> result = productListingService.findByCriteria(pageable, null, null, conditions, null, null);

        assertEquals(1, result.getTotalElements());
        assertEquals("image-url", result.getContent().get(0).getImageUrl());
        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productImageRepository).findAll(any(Specification.class));
    }

    @Test
    public void testFindByCriteriaWithPriceRange() {
        Product product = new Product();
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products);
        Double minPrice = 100.0;
        Double maxPrice = 500.0;

        ProductListingDTO productListingDTO = new ProductListingDTO();
        ProductImage productImage = new ProductImage();
        productImage.setImageId(1L);
        productImage.setImageUrl("image-url");
        productImage.setProduct(product);
        List<ProductImage> productImages = List.of(productImage);

        Specification<Product> spec = ProductSpecification.hasPriceBetween(minPrice, maxPrice);
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productListingMapper.mapTo(product)).thenReturn(productListingDTO);
        when(productImageRepository.findAll(any(Specification.class))).thenReturn(productImages);

        Page<ProductListingDTO> result = productListingService.findByCriteria(pageable, null, null, null, minPrice, maxPrice);

        assertEquals(1, result.getTotalElements());
        assertEquals("image-url", result.getContent().get(0).getImageUrl());
        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productImageRepository).findAll(any(Specification.class));
    }

    @Test
    public void testFindByCriteriaWithSearch() {
        Product product = new Product();
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products);
        String search = "phone";

        ProductListingDTO productListingDTO = new ProductListingDTO();
        ProductImage productImage = new ProductImage();
        productImage.setImageId(1L);
        productImage.setImageUrl("image-url");
        productImage.setProduct(product);
        List<ProductImage> productImages = List.of(productImage);

        Specification<Product> spec = ProductSpecification.hasTitleOrDescriptionContaining(search);
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productListingMapper.mapTo(product)).thenReturn(productListingDTO);
        when(productImageRepository.findAll(any(Specification.class))).thenReturn(productImages);

        Page<ProductListingDTO> result = productListingService.findByCriteria(pageable, search, null, null, null, null);

        assertEquals(1, result.getTotalElements());
        assertEquals("image-url", result.getContent().get(0).getImageUrl());
        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productImageRepository).findAll(any(Specification.class));
    }

    @Test
    public void testFindByCriteriaWithAllSpecifications() {
        Product product = new Product();
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products);
        String search = "phone";
        List<String> categories = List.of("Electronics");
        List<String> conditions = List.of("New");
        Double minPrice = 100.0;
        Double maxPrice = 500.0;

        ProductListingDTO productListingDTO = new ProductListingDTO();
        ProductImage productImage = new ProductImage();
        productImage.setImageId(1L);
        productImage.setImageUrl("image-url");
        productImage.setProduct(product);
        List<ProductImage> productImages = List.of(productImage);

        Specification<Product> spec = Specification
                .where(ProductSpecification.hasTitleOrDescriptionContaining(search))
                .and(ProductSpecification.hasCategory(categories))
                .and(ProductSpecification.hasCondition(conditions))
                .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice));
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productListingMapper.mapTo(product)).thenReturn(productListingDTO);
        when(productImageRepository.findAll(any(Specification.class))).thenReturn(productImages);

        Page<ProductListingDTO> result = productListingService.findByCriteria(pageable, search, categories, conditions, minPrice, maxPrice);

        assertEquals(1, result.getTotalElements());
        assertEquals("image-url", result.getContent().get(0).getImageUrl());
        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productImageRepository).findAll(any(Specification.class));
    }
}
