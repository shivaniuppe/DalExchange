package com.asdc.dalexchange.service;

import com.asdc.dalexchange.dto.ProductListingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductListingService {
    Page<ProductListingDTO> findByCriteria(
            Pageable pageable,
            String search,
            List<String> categories,
            List<String> conditions,
            Double minPrice,
            Double maxPrice);
}
