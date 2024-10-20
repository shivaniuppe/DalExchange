package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.ProductListingDTO;
import com.asdc.dalexchange.dto.TradeRequestDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductImage;
import com.asdc.dalexchange.model.TradeRequest;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.ProductImageRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.TradeRequestRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.specifications.TradeRequestSpecification;
import com.asdc.dalexchange.util.AuthUtil;
import com.asdc.dalexchange.util.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TradeRequestServiceImplTest {

    @Mock
    private TradeRequestRepository tradeRequestRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private Mapper<TradeRequest, TradeRequestDTO> tradeRequestMapper;

    @InjectMocks
    private TradeRequestServiceImpl tradeRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBuyerTradeRequests() {

        User buyer = new User();
        buyer.setUserId(1L);
        Long buyerId = 1L;

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(buyer));

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUserId(any(UserRepository.class))).thenReturn(buyerId);

            TradeRequest tradeRequest = new TradeRequest();
            TradeRequestDTO tradeRequestDTO = new TradeRequestDTO();
            tradeRequestDTO.setProduct(new ProductListingDTO());
            tradeRequestDTO.getProduct().setProductId(1L);

            ProductImage productImage = new ProductImage();
            productImage.setImageUrl("sample_image_url");

            when(tradeRequestRepository.findAll(any(Specification.class))).thenReturn(List.of(tradeRequest));
            when(tradeRequestMapper.mapTo(any(TradeRequest.class))).thenReturn(tradeRequestDTO);
            when(productImageRepository.findAll(any(Specification.class))).thenReturn(List.of(productImage));

            List<TradeRequestDTO> result = tradeRequestService.getBuyerTradeRequests();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(tradeRequestDTO, result.get(0));
            assertEquals("sample_image_url", result.get(0).getProduct().getImageUrl());

            verify(tradeRequestRepository, times(1)).findAll(any(Specification.class));
            verify(productImageRepository, times(1)).findAll(any(Specification.class));
            verify(tradeRequestMapper, times(1)).mapTo(any(TradeRequest.class));
        }
    }

    @Test
    public void testGetSellerTradeRequests() {

        User seller = new User();
        seller.setUserId(1L);
        Long sellerId = 1L;

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(seller));

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUserId(any(UserRepository.class))).thenReturn(sellerId);

            TradeRequest tradeRequest = new TradeRequest();
            TradeRequestDTO tradeRequestDTO = new TradeRequestDTO();
            tradeRequestDTO.setProduct(new ProductListingDTO());
            tradeRequestDTO.getProduct().setProductId(1L);

            ProductImage productImage = new ProductImage();
            productImage.setImageUrl("sample_image_url");

            when(tradeRequestRepository.findAll(any(Specification.class))).thenReturn(List.of(tradeRequest));
            when(tradeRequestMapper.mapTo(any(TradeRequest.class))).thenReturn(tradeRequestDTO);
            when(productImageRepository.findAll(any(Specification.class))).thenReturn(List.of(productImage));

            List<TradeRequestDTO> result = tradeRequestService.getSellerTradeRequests();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(tradeRequestDTO, result.get(0));
            assertEquals("sample_image_url", result.get(0).getProduct().getImageUrl());

            verify(tradeRequestRepository, times(1)).findAll(any(Specification.class));
            verify(productImageRepository, times(1)).findAll(any(Specification.class));
            verify(tradeRequestMapper, times(1)).mapTo(any(TradeRequest.class));
        }

    }

    @Test
    public void testUpdateTradeRequestStatus() {
        Long requestId = 1L;
        String status = "approved";
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setRequestStatus("pending");
        TradeRequest updatedTradeRequest = new TradeRequest();
        updatedTradeRequest.setRequestStatus(status);

        when(tradeRequestRepository.findById(requestId)).thenReturn(Optional.of(tradeRequest));
        when(tradeRequestRepository.save(tradeRequest)).thenReturn(updatedTradeRequest);

        TradeRequest result = tradeRequestService.updateTradeRequestStatus(requestId, status);

        assertNotNull(result);
        assertEquals(status, result.getRequestStatus());

        verify(tradeRequestRepository, times(1)).findById(requestId);
        verify(tradeRequestRepository, times(1)).save(tradeRequest);
    }

    @Test
    public void testCreateTradeRequest() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", 1L);
        requestBody.put("sellerId", 1L);
        requestBody.put("requestedPrice", 100.0);

        Product product = new Product();
        User seller = new User();
        User buyer = new User();
        TradeRequest createdTradeRequest = new TradeRequest();
        TradeRequestDTO createdTradeRequestDTO = new TradeRequestDTO();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(seller));

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUser(userRepository)).thenReturn(buyer);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
            when(userRepository.findById(0L)).thenReturn(Optional.of(buyer));
            when(tradeRequestRepository.save(any(TradeRequest.class))).thenReturn(createdTradeRequest);
            when(tradeRequestMapper.mapTo(createdTradeRequest)).thenReturn(createdTradeRequestDTO);

            TradeRequestDTO result = tradeRequestService.createTradeRequest(requestBody);

            assertNotNull(result);
            assertEquals(createdTradeRequestDTO, result);

            verify(productRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).findById(0L);
            verify(tradeRequestRepository, times(1)).save(any(TradeRequest.class));
            verify(tradeRequestMapper, times(1)).mapTo(createdTradeRequest);
        }
    }

    @Test
    public void testGetApprovedTradeRequestAmount() {
        Long productId = 1L;
        Long userId = 123L;


        try (var mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUserId(any())).thenReturn(userId);

            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setRequestedPrice(100.00);

            when(tradeRequestRepository.findAll(any(Specification.class)))
                    .thenReturn(Collections.singletonList(tradeRequest));

            double result = tradeRequestService.getApprovedTradeRequestAmount(productId);

            assertEquals(100.00, result, 0.01);
        }
    }


    @Test
    public void testGetApprovedTradeRequestAmount_noTradeRequests() {
        Long productId = 1L;
        Long userId = 123L;

        try (var mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            when(tradeRequestRepository.findAll(Specification
                    .where(TradeRequestSpecification.hasBuyerId(userId))
                    .and(TradeRequestSpecification.hasProductId(productId))
                    .and(TradeRequestSpecification.hasRequestStatus("approved"))))
                    .thenReturn(Collections.emptyList());

            assertThrows(ResourceNotFoundException.class, () -> tradeRequestService.getApprovedTradeRequestAmount(productId));
        }
    }

    @Test
    public void testUpdateStatusByProduct_success() {
        Long productId = 1L;
        Long userId = 123L;

        try (var mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setRequestStatus("approved");

            Specification<TradeRequest> spec = Specification
                    .where(TradeRequestSpecification.hasProductId(productId))
                    .and(TradeRequestSpecification.hasBuyerId(userId));
            when(tradeRequestRepository.findOne(any(Specification.class))).thenReturn(Optional.of(tradeRequest));
            when(tradeRequestRepository.save(any(TradeRequest.class))).thenAnswer(invocation -> {
                TradeRequest savedRequest = invocation.getArgument(0);
                savedRequest.setRequestStatus("completed");
                return savedRequest;
            });

            Map<String, Object> requestBody = Map.of("productId", productId);

            String result = tradeRequestService.updateStatusByProduct(requestBody);

            assertEquals("Trade request updated successfully", result);
            verify(tradeRequestRepository).findOne(any(Specification.class));
            verify(tradeRequestRepository).save(any(TradeRequest.class));
        }
    }

    @Test
    public void testUpdateStatusByProduct_notFound() {
        Long productId = 1L;
        Long userId = 123L;

        try (var mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            Specification<TradeRequest> spec = Specification
                    .where(TradeRequestSpecification.hasProductId(productId))
                    .and(TradeRequestSpecification.hasBuyerId(userId));
            when(tradeRequestRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

            Map<String, Object> requestBody = Map.of("productId", productId);

            String result = tradeRequestService.updateStatusByProduct(requestBody);

            assertEquals("TradeRequest not found for product ID: 1 and user ID: 123", result);
            verify(tradeRequestRepository).findOne(any(Specification.class));
        }
    }

}
