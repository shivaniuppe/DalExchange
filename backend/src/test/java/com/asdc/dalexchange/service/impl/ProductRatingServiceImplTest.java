package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.ProductRatingAdminDTO;
import com.asdc.dalexchange.dto.ProductRatingDTO;
import com.asdc.dalexchange.enums.ProductCondition;
import com.asdc.dalexchange.enums.ShippingType;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.*;
import com.asdc.dalexchange.repository.ProductRatingRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.specifications.ProductSpecification;
import com.asdc.dalexchange.util.AuthUtil;
import com.asdc.dalexchange.util.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductRatingServiceImplTest {

    @Mock
    private ProductRatingRepository productRatingRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Mapper<ProductRating, ProductRatingDTO> productRatingMapper;

    @Mock
    private Mapper<ProductRating, ProductRatingAdminDTO> productRatingAdminMapper;

    @InjectMocks
    private ProductRatingServiceImpl productRatingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAllReviewOfAllSoldItemsOfUser_NoReviews() {
        Long userId = 1L;
        User seller = new User();
        seller.setUserId(userId);

        ProductCategory category = new ProductCategory();
        category.setCategoryId(1L);
        category.setName("Electronics");

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);
            List<ProductRatingDTO> result = productRatingService.allReviewOfAllSoldItemsOfUser();
            Product product1 = new Product();
            product1.setProductId(101L);
            product1.setSeller(seller);
            product1.setTitle("Product 1");
            product1.setDescription("Description 1");
            product1.setPrice(100.0);
            product1.setCategory(category);
            product1.setProductCondition(ProductCondition.New);
            product1.setUseDuration("1 month");
            product1.setShippingType(ShippingType.Free);
            product1.setQuantityAvailable(10);
            product1.setCreatedAt(LocalDateTime.now());
            product1.setUnlisted(false);

            Product product2 = new Product();
            product2.setProductId(102L);
            product2.setSeller(seller);
            product2.setTitle("Product 2");
            product2.setDescription("Description 2");
            product2.setPrice(200.0);
            product2.setCategory(category);
            product2.setProductCondition(ProductCondition.Used);
            product2.setUseDuration("6 months");
            product2.setShippingType(ShippingType.Paid);
            product2.setQuantityAvailable(5);
            product2.setCreatedAt(LocalDateTime.now());
            product2.setUnlisted(false);

            when(productRepository.findAll(ProductSpecification.bySellerUserId(userId)))
                    .thenReturn(Arrays.asList(product1, product2));
            when(productRatingRepository.findByIdProductId(101L)).thenReturn(Collections.emptyList());
            when(productRatingRepository.findByIdProductId(102L)).thenReturn(Collections.emptyList());
            assertEquals(0, result.size());
        }
    }

    @Test
    void testGetAllReviews_Empty() {
        when(productRatingRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductRatingAdminDTO> result = productRatingService.getAllReviews();

        assertEquals(0, result.size());
        verify(productRatingRepository, times(1)).findAll();
    }

    @Test
    void testDeleteReview() {
        Long productId = 1L;
        Long userId = 1L;
        ProductRatingID id = new ProductRatingID(productId, userId);

        productRatingService.deleteReview(productId, userId);

        verify(productRatingRepository, times(1)).deleteById(id);
    }


    @Test
    @Transactional
    void testDeleteReview_Exists() {
        Long productId = 1L;
        Long userId = 2L;
        ProductRatingID id = new ProductRatingID(productId, userId);

        doNothing().when(productRatingRepository).deleteById(id);

        productRatingService.deleteReview(productId, userId);

        verify(productRatingRepository, times(1)).deleteById(id);
    }

    @Test
    void testSaveRating_Success() {
        Long userId = 1L;
        Long productId = 2L;
        Integer rating = 5;
        String review = "Excellent product!";

        User user = new User();
        user.setUserId(userId);

        Product product = new Product();
        product.setProductId(productId);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);
        requestBody.put("rating", rating);
        requestBody.put("review", review);

        ProductRating productRating = new ProductRating();
        ProductRatingID productRatingID = new ProductRatingID(productId, userId);
        productRating.setId(productRatingID);
        productRating.setUser(user);
        productRating.setProduct(product);
        productRating.setRating(rating);
        productRating.setReview(review);

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            productRatingService.saveRating(requestBody);

            verify(userRepository, times(1)).findById(userId);
            verify(productRepository, times(1)).findById(productId);
            verify(productRatingRepository, times(1)).save(any(ProductRating.class));
        }
    }

    @Test
    void testSaveRating_UserNotFound() {
        Long userId = 1L;
        Long productId = 2L;
        Integer rating = 5;
        String review = "Good product";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);
        requestBody.put("rating", rating);
        requestBody.put("review", review);

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> productRatingService.saveRating(requestBody),
                    "Expected saveRating to throw ResourceNotFoundException, but it didn't");

            verify(userRepository, times(1)).findById(userId);
            verify(productRepository, times(0)).findById(productId);
            verify(productRatingRepository, times(0)).save(any(ProductRating.class));
        }
    }

    @Test
    void testSaveRating_ProductNotFound() {
        Long userId = 1L;
        Long productId = 2L;
        Integer rating = 5;
        String review = "Good product";

        User user = new User();
        user.setUserId(userId);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);
        requestBody.put("rating", rating);
        requestBody.put("review", review);

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> productRatingService.saveRating(requestBody),
                    "Expected saveRating to throw ResourceNotFoundException, but it didn't");

            verify(userRepository, times(1)).findById(userId);
            verify(productRepository, times(1)).findById(productId);
            verify(productRatingRepository, times(0)).save(any(ProductRating.class));
        }
    }

    @Test
    void testGetAllReviewsByProduct() {
        Long productId = 1L;
        List<ProductRating> productRatings = Arrays.asList(new ProductRating(), new ProductRating());
        ProductRatingAdminDTO dto1 = new ProductRatingAdminDTO();
        ProductRatingAdminDTO dto2 = new ProductRatingAdminDTO();

        when(productRatingRepository.findByIdProductId(productId)).thenReturn(productRatings);
        when(productRatingAdminMapper.mapTo(productRatings.get(0))).thenReturn(dto1);
        when(productRatingAdminMapper.mapTo(productRatings.get(1))).thenReturn(dto2);

        List<ProductRatingAdminDTO> result = productRatingService.getAllReviewsByProduct(productId);

        assertEquals(2, result.size());
    }

    @Test
    void testGetAllReviewsByProduct_NoReviews() {
        Long productId = 1L;

        when(productRatingRepository.findByIdProductId(productId)).thenReturn(Arrays.asList());

        List<ProductRatingAdminDTO> result = productRatingService.getAllReviewsByProduct(productId);

        assertTrue(result.isEmpty());

    }

}