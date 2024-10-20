package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.SoldItemDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.SoldItem;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.SoldItemRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.specifications.SoldItemSpecification;
import com.asdc.dalexchange.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

;

public class SoldItemServiceImplTest {

    @Mock
    private SoldItemRepository soldItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Mapper<SoldItem, SoldItemDTO> soldItemMapper;


    @InjectMocks
    private SoldItemServiceImpl soldItemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testGetAllSoldProduct_EmptyList() {
        Long userId = 1L;

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);
            when(soldItemMapper.mapTo(new SoldItem())).thenReturn(new SoldItemDTO());
            when(soldItemRepository.findAll(SoldItemSpecification.bySellerUserId(userId)))
                    .thenReturn(Collections.emptyList());

            List<SoldItemDTO> result = soldItemService.GetAllSoldProduct();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    public void testSaveSoldItem_ProductNotExists() {
        Long productId = 1L;
        Product product = new Product();
        product.setSeller(new User());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(soldItemRepository.existsByProduct_ProductId(productId)).thenReturn(false);

        soldItemService.saveSoldItem(requestBody);

        ArgumentCaptor<SoldItem> soldItemCaptor = ArgumentCaptor.forClass(SoldItem.class);
        verify(soldItemRepository, times(1)).save(soldItemCaptor.capture());

        SoldItem savedSoldItem = soldItemCaptor.getValue();
        assertNotNull(savedSoldItem);
        assertEquals(product, savedSoldItem.getProduct());
        assertEquals(product.getSeller(), savedSoldItem.getSeller());
        assertNotNull(savedSoldItem.getSoldDate());
    }

    @Test
    public void testSaveSoldItem_ProductAlreadyExists() {
        Long productId = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));
        when(soldItemRepository.existsByProduct_ProductId(productId)).thenReturn(true);

        soldItemService.saveSoldItem(requestBody);

        verify(soldItemRepository, never()).save(any(SoldItem.class));
    }

    @Test
    public void testSaveSoldItem_SaveException() {
        Long productId = 1L;
        Product product = new Product();
        product.setSeller(new User());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(soldItemRepository.existsByProduct_ProductId(productId)).thenReturn(false);
        doThrow(new RuntimeException("Database error")).when(soldItemRepository).save(any(SoldItem.class));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> soldItemService.saveSoldItem(requestBody));
        assertEquals("Error saving SoldItem", thrown.getMessage());
    }
    @Test
    public void testSaveSoldItem_ProductNotFound() {
        Long productId = 1L;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> soldItemService.saveSoldItem(requestBody));
        assertEquals("Product not found", thrown.getMessage());
    }

}