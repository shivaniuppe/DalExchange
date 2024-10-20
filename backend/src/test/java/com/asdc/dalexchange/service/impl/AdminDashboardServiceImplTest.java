package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.AdminDashboardDTO;
import com.asdc.dalexchange.dto.BestSellingProductsDTO;
import com.asdc.dalexchange.dto.ItemsSoldDTO;
import com.asdc.dalexchange.dto.TopSellingCategoriesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminDashboardServiceImplTest {

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private OrderServiceImpl orderServiceImpl;

    @InjectMocks
    private AdminDashboardServiceImpl adminDashboardServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdminStats() {
        when(userServiceImpl.newCustomers()).thenReturn(10L);
        when(orderServiceImpl.newOrders()).thenReturn(20L);
        when(orderServiceImpl.totalSales()).thenReturn(5000.0);
        when(orderServiceImpl.avgSales()).thenReturn(250.0);
        when(orderServiceImpl.salesChange()).thenReturn(15.2);

        AdminDashboardDTO adminStats = adminDashboardServiceImpl.adminStats();

        assertEquals(10L, adminStats.getCustomers());
        assertEquals(20L, adminStats.getOrders());
        assertEquals(5000.0, adminStats.getSales());
        assertEquals(250.0, adminStats.getAvgOrderValue());
        assertEquals(15.2, adminStats.getSalesChange());
    }

    @Test
    void testGetItemsSold() {
        ItemsSoldDTO dto1 = new ItemsSoldDTO();
        ItemsSoldDTO dto2 = new ItemsSoldDTO();
        List<ItemsSoldDTO> mockItemsSold = Arrays.asList(dto1, dto2);

        when(orderServiceImpl.getItemsSold()).thenReturn(mockItemsSold);

        List<ItemsSoldDTO> result = adminDashboardServiceImpl.getItemsSold();

        assertEquals(2, result.size());
        verify(orderServiceImpl, times(1)).getItemsSold();
    }

    @Test
    void testGetTopSellingCategories() {
        TopSellingCategoriesDTO dto1 = new TopSellingCategoriesDTO();
        TopSellingCategoriesDTO dto2 = new TopSellingCategoriesDTO();
        List<TopSellingCategoriesDTO> mockTopSellingCategories = Arrays.asList(dto1, dto2);

        when(orderServiceImpl.getTopSellingCategories()).thenReturn(mockTopSellingCategories);

        List<TopSellingCategoriesDTO> result = adminDashboardServiceImpl.getTopSellingCategories();

        assertEquals(2, result.size());
        verify(orderServiceImpl, times(1)).getTopSellingCategories();
    }

    @Test
    void testGetBestSellingProducts() {
        BestSellingProductsDTO dto1 = new BestSellingProductsDTO();
        BestSellingProductsDTO dto2 = new BestSellingProductsDTO();
        List<BestSellingProductsDTO> mockBestSellingProducts = Arrays.asList(dto1, dto2);

        when(orderServiceImpl.getBestSellingProducts()).thenReturn(mockBestSellingProducts);

        List<BestSellingProductsDTO> result = adminDashboardServiceImpl.getBestSellingProducts();

        assertEquals(2, result.size());
        verify(orderServiceImpl, times(1)).getBestSellingProducts();
    }
}
