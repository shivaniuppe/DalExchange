package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.AdminDashboardDTO;
import com.asdc.dalexchange.dto.BestSellingProductsDTO;
import com.asdc.dalexchange.dto.ItemsSoldDTO;
import com.asdc.dalexchange.dto.TopSellingCategoriesDTO;
import com.asdc.dalexchange.service.AdminDashboardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the AdminDashboardService interface.
 * Provides methods to retrieve various statistics and reports for the admin dashboard.
 */
@Service
@Slf4j
@AllArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserServiceImpl userServiceImpl;
    private final OrderServiceImpl orderServiceImpl;

    /**
     * Retrieves various statistics for the admin dashboard.
     *
     * @return AdminDashboardDTO containing the admin dashboard statistics.
     */
    @Override
    public AdminDashboardDTO adminStats() {
        log.info("Fetching admin dashboard statistics");
        AdminDashboardDTO adminStats = new AdminDashboardDTO();
        adminStats.setCustomers(userServiceImpl.newCustomers());
        adminStats.setOrders(orderServiceImpl.newOrders());
        adminStats.setSales(orderServiceImpl.totalSales());
        adminStats.setAvgOrderValue(orderServiceImpl.avgSales());
        adminStats.setSalesChange(orderServiceImpl.salesChange());
        adminStats.setOrdersChange(orderServiceImpl.ordersChange());
        adminStats.setCustomersChange(userServiceImpl.customersChange());
        adminStats.setAvgOrderValueChange(orderServiceImpl.avgOrderValueChange());
        log.info("Admin dashboard statistics fetched successfully");
        return adminStats;
    }

    /**
     * Retrieves data on items sold.
     *
     * @return List of ItemsSoldDTO containing data on items sold.
     */
    @Override
    public List<ItemsSoldDTO> getItemsSold() {
        log.info("Fetching items sold data");
        List<ItemsSoldDTO> itemsSold = orderServiceImpl.getItemsSold();
        log.info("Items sold data fetched successfully");
        return itemsSold;
    }

    /**
     * Retrieves data on top-selling categories.
     *
     * @return List of TopSellingCategoriesDTO containing data on top-selling categories.
     */
    @Override
    public List<TopSellingCategoriesDTO> getTopSellingCategories() {
        log.info("Fetching top-selling categories data");
        List<TopSellingCategoriesDTO> topSellingCategories = orderServiceImpl.getTopSellingCategories();
        log.info("Top-selling categories data fetched successfully");
        return topSellingCategories;
    }

    /**
     * Retrieves data on best-selling products.
     *
     * @return List of BestSellingProductsDTO containing data on best-selling products.
     */
    @Override
    public List<BestSellingProductsDTO> getBestSellingProducts() {
        log.info("Fetching best-selling products data");
        List<BestSellingProductsDTO> bestSellingProducts = orderServiceImpl.getBestSellingProducts();
        log.info("Best-selling products data fetched successfully");
        return bestSellingProducts;
    }
}
