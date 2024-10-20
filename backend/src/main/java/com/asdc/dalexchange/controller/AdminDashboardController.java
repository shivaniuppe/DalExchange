package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.AdminDashboardDTO;
import com.asdc.dalexchange.dto.BestSellingProductsDTO;
import com.asdc.dalexchange.dto.ItemsSoldDTO;
import com.asdc.dalexchange.dto.TopSellingCategoriesDTO;
import com.asdc.dalexchange.service.AdminDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Controller class to handle admin dashboard related requests.
 */
@RestController
@RequestMapping("/admin")
@Slf4j
@AllArgsConstructor
public class AdminDashboardController {

    private AdminDashboardService adminDashboardService;

    /**
     * Retrieves the admin statistics.
     *
     * @return AdminDashboardDTO containing various statistics.
     */
    @GetMapping("/stats")
    public AdminDashboardDTO getUsers() {
        log.info("Fetching admin statistics");
        AdminDashboardDTO adminStats = adminDashboardService.adminStats();
        log.info("Admin statistics fetched successfully: {}", adminStats);
        return adminStats;
    }

    /**
     * Retrieves the number of items sold per month.
     *
     * @return List of ItemsSoldDTO containing month and items sold.
     */
    @GetMapping("/items-sold")
    public List<ItemsSoldDTO> getItemsSold() {
        log.info("Fetching items sold data");
        List<ItemsSoldDTO> itemsSold = adminDashboardService.getItemsSold();
        log.info("Items sold data fetched successfully");
        return itemsSold;
    }

    /**
     * Retrieves the top-selling categories.
     *
     * @return List of TopSellingCategoriesDTO containing category and sales data.
     */
    @GetMapping("/top-selling-categories")
    public List<TopSellingCategoriesDTO> getTopSellingCategories() {
        log.info("Fetching top-selling categories");
        List<TopSellingCategoriesDTO> topSellingCategories = adminDashboardService.getTopSellingCategories();
        log.info("Top-selling categories fetched successfully");
        return topSellingCategories;
    }

    /**
     * Retrieves the best-selling products.
     *
     * @return List of BestSellingProductsDTO containing product name and items sold.
     */
    @GetMapping("/best-selling-products")
    public List<BestSellingProductsDTO> getBestSellingProducts() {
        log.info("Fetching best-selling products");
        List<BestSellingProductsDTO> bestSellingProducts = adminDashboardService.getBestSellingProducts();
        log.info("Best-selling products fetched successfully");
        return bestSellingProducts;
    }
}
