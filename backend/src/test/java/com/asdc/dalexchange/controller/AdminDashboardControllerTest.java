package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.AdminDashboardDTO;
import com.asdc.dalexchange.dto.BestSellingProductsDTO;
import com.asdc.dalexchange.dto.ItemsSoldDTO;
import com.asdc.dalexchange.dto.TopSellingCategoriesDTO;
import com.asdc.dalexchange.service.AdminDashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminDashboardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminDashboardService adminDashboardService;

    @InjectMocks
    private AdminDashboardController adminDashboardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminDashboardController).build();
    }

    @Test
    public void testGetUsers() throws Exception {
        AdminDashboardDTO adminDashboardDTO = new AdminDashboardDTO();
        adminDashboardDTO.setCustomers(100);
        adminDashboardDTO.setOrders(50);
        adminDashboardDTO.setSales(2000.0);
        adminDashboardDTO.setAvgOrderValue(40.0);
        adminDashboardDTO.setSalesChange(5.0);
        adminDashboardDTO.setOrdersChange(10.0);
        adminDashboardDTO.setCustomersChange(3.0);
        adminDashboardDTO.setAvgOrderValueChange(2.0);

        when(adminDashboardService.adminStats()).thenReturn(adminDashboardDTO);

        mockMvc.perform(get("/admin/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"customers\":100,\"orders\":50,\"sales\":2000.0,\"avgOrderValue\":40.0,\"salesChange\":5.0,\"ordersChange\":10.0,\"customersChange\":3.0,\"avgOrderValueChange\":2.0}"));
    }

    @Test
    public void testGetItemsSold() throws Exception {
        List<ItemsSoldDTO> itemsSoldDTOList = new ArrayList<>();
        ItemsSoldDTO item1 = new ItemsSoldDTO();
        item1.setMonth("January");
        item1.setItemsSold(100);
        ItemsSoldDTO item2 = new ItemsSoldDTO();
        item2.setMonth("February");
        item2.setItemsSold(150);
        itemsSoldDTOList.add(item1);
        itemsSoldDTOList.add(item2);

        when(adminDashboardService.getItemsSold()).thenReturn(itemsSoldDTOList);

        mockMvc.perform(get("/admin/items-sold")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"month\":\"January\",\"itemsSold\":100},{\"month\":\"February\",\"itemsSold\":150}]"));
    }

    @Test
    public void testGetTopSellingCategories() throws Exception {
        List<TopSellingCategoriesDTO> topSellingCategoriesDTOList = new ArrayList<>();
        TopSellingCategoriesDTO category1 = new TopSellingCategoriesDTO();
        category1.setCategory("Electronics");
        category1.setSales(200);
        TopSellingCategoriesDTO category2 = new TopSellingCategoriesDTO();
        category2.setCategory("Books");
        category2.setSales(300);
        topSellingCategoriesDTOList.add(category1);
        topSellingCategoriesDTOList.add(category2);

        when(adminDashboardService.getTopSellingCategories()).thenReturn(topSellingCategoriesDTOList);

        mockMvc.perform(get("/admin/top-selling-categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"category\":\"Electronics\",\"sales\":200},{\"category\":\"Books\",\"sales\":300}]"));
    }

    @Test
    public void testGetBestSellingProducts() throws Exception {
        List<BestSellingProductsDTO> bestSellingProductsDTOList = new ArrayList<>();
        BestSellingProductsDTO product1 = new BestSellingProductsDTO();
        product1.setProductName("Laptop");
        product1.setItemsSold(100);
        BestSellingProductsDTO product2 = new BestSellingProductsDTO();
        product2.setProductName("Smartphone");
        product2.setItemsSold(150);
        bestSellingProductsDTOList.add(product1);
        bestSellingProductsDTOList.add(product2);

        when(adminDashboardService.getBestSellingProducts()).thenReturn(bestSellingProductsDTOList);

        mockMvc.perform(get("/admin/best-selling-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"productName\":\"Laptop\",\"itemsSold\":100},{\"productName\":\"Smartphone\",\"itemsSold\":150}]"));
    }
}
