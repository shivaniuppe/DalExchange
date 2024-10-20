package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.ProductDetailsDTO;
import com.asdc.dalexchange.enums.ProductCondition;
import com.asdc.dalexchange.enums.ShippingType;
import com.asdc.dalexchange.mappers.impl.ProductDetailsMapperImpl;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductCategory;
import com.asdc.dalexchange.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProductDetailsMapperImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductDetailsMapperImpl productDetailsMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMapToProductDetailsDTO() {

        Long productId = 1L;
        LocalDateTime joiningDate = LocalDateTime.now();
        Product product = createMockProduct(productId, joiningDate);

        ProductDetailsDTO expectedDTO = createExpectedProductDetailsDTO(productId, joiningDate);
        when(modelMapper.map(product, ProductDetailsDTO.class)).thenReturn(expectedDTO);

        ProductDetailsDTO resultDTO = productDetailsMapper.mapTo(product);

        assertEquals(expectedDTO.getProductId(), resultDTO.getProductId());
        assertEquals(expectedDTO.getSellerJoiningDate(), resultDTO.getSellerJoiningDate());
        assertEquals(expectedDTO.getCategory(), resultDTO.getCategory());
    }

    private Product createMockProduct(Long productId, LocalDateTime joiningDate) {
        Product product = new Product();
        product.setProductId(productId);
        product.setSeller(createMockUser());
        product.setTitle("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);
        product.setCategory(createMockProductCategory());
        product.setProductCondition(ProductCondition.New);
        product.setUseDuration("1 year");
        product.setShippingType(ShippingType.Free);
        product.setQuantityAvailable(10);
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }

    private ProductCategory createMockProductCategory() {
        ProductCategory category = new ProductCategory();
        category.setCategoryId(1L);
        category.setName("Test Category");
        return category;
    }

    private User createMockUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        return user;
    }

    private ProductDetailsDTO createExpectedProductDetailsDTO(Long productId, LocalDateTime joiningDate) {
        ProductDetailsDTO dto = new ProductDetailsDTO();
        dto.setProductId(productId);
        dto.setSellerJoiningDate(joiningDate);
        dto.setCategory("Test Category");
        return dto;
    }
}
