package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.ProductDetailsDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductCategory;
import com.asdc.dalexchange.model.ProductImage;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.ProductImageRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.ProductWishlistRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.util.AuthUtil;
import com.asdc.dalexchange.util.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductDetailsServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private ProductWishlistRepository productWishlistRepository;

    @Mock
    private Mapper<Product, ProductDetailsDTO> productDetailsMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductDetailsServiceImpl productDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDetailsProductNotFound() {
        Long productId = 1L;
        Long userId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            assertThrows(ResourceNotFoundException.class, () -> productDetailsService.getDetails(productId));

            verify(productRepository).findById(productId);
            verifyNoMoreInteractions(productImageRepository, productWishlistRepository, productDetailsMapper);
        }
    }

    @Test
    void testGetDetailsSuccess() {
        Long userId = 1L;
        Long productId = 1L;
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Books");
        product.setCategory(productCategory);
        User seller = new User();
        seller.setUserId(2L);
        seller.setJoinedAt(LocalDateTime.now());
        seller.setSellerRating(4.5);
        product.setSeller(seller);

        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productDetailsDTO.setCategory("Books");
        productDetailsDTO.setSellerId(seller.getUserId());
        productDetailsDTO.setSellerJoiningDate(seller.getJoinedAt());
        productDetailsDTO.setRating(seller.getSellerRating());
        productDetailsDTO.setImageUrls(List.of("url1", "url2"));
        productDetailsDTO.setFavorite(true);

        ProductImage productImage1 = new ProductImage();
        productImage1.setProduct(product);
        productImage1.setImageUrl("url1");

        ProductImage productImage2 = new ProductImage();
        productImage2.setProduct(product);
        productImage2.setImageUrl("url2");

        List<ProductImage> productImages = List.of(productImage1, productImage2);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productImageRepository.findAll(any(Specification.class))).thenReturn(productImages);
        when(productDetailsMapper.mapTo(product)).thenReturn(productDetailsDTO);
        when(productWishlistRepository.count(any(Specification.class))).thenReturn(1L);

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            ProductDetailsDTO result = productDetailsService.getDetails(productId);

            assertNotNull(result);
            assertEquals("Books", result.getCategory());
            assertEquals(2L, result.getSellerId());
            assertEquals(seller.getJoinedAt(), result.getSellerJoiningDate());
            assertEquals(4.5, result.getRating());
            assertEquals(List.of("url1", "url2"), result.getImageUrls());
            assertTrue(result.isFavorite());

            verify(productRepository).findById(productId);
            verify(productImageRepository).findAll(any(Specification.class));
            verify(productDetailsMapper).mapTo(product);
            verify(productWishlistRepository).count(any(Specification.class));
        }
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productDetailsService.getProductById(productId);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository).findById(productId);
    }

    @Test
    void testGetProductByIdNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productDetailsService.getProductById(productId));

        verify(productRepository).findById(productId);
    }

    @Test
    void testGetImageUrls() {
        Long productId = 1L;
        ProductImage productImage1 = new ProductImage();
        productImage1.setImageUrl("url1");
        ProductImage productImage2 = new ProductImage();
        productImage2.setImageUrl("url2");
        List<ProductImage> productImages = List.of(productImage1, productImage2);

        when(productImageRepository.findAll(any(Specification.class))).thenReturn(productImages);

        List<String> result = productDetailsService.getImageUrls(productId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("url1", result.get(0));
        assertEquals("url2", result.get(1));

        verify(productImageRepository).findAll(any(Specification.class));
    }

    @Test
    void testGetFavoriteStatus() {
        Long userId = 1L;
        Long productId = 1L;

        when(productWishlistRepository.count(any(Specification.class))).thenReturn(1L);

        boolean result = productDetailsService.getFavoriteStatus(userId, productId);

        assertTrue(result);

        verify(productWishlistRepository).count(any(Specification.class));
    }

    @Test
    void testGetFavoriteStatusNotFavorite() {
        Long userId = 1L;
        Long productId = 1L;

        when(productWishlistRepository.count(any(Specification.class))).thenReturn(0L);

        boolean result = productDetailsService.getFavoriteStatus(userId, productId);

        assertFalse(result);

        verify(productWishlistRepository).count(any(Specification.class));
    }
}