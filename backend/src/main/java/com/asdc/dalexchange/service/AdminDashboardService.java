package com.asdc.dalexchange.service;

import com.asdc.dalexchange.dto.AdminDashboardDTO;
import com.asdc.dalexchange.dto.BestSellingProductsDTO;
import com.asdc.dalexchange.dto.ItemsSoldDTO;
import com.asdc.dalexchange.dto.TopSellingCategoriesDTO;

import java.util.List;

public interface AdminDashboardService {
    AdminDashboardDTO adminStats();
    List<ItemsSoldDTO> getItemsSold();
    List<TopSellingCategoriesDTO> getTopSellingCategories();
    List<BestSellingProductsDTO> getBestSellingProducts();
}
