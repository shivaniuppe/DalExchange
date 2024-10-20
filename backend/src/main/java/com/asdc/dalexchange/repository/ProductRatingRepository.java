package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.ProductRating;
import com.asdc.dalexchange.model.ProductRatingID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRatingRepository extends JpaRepository<ProductRating, ProductRatingID>, JpaSpecificationExecutor<ProductRating> {
    List<ProductRating> findByIdUserId(Long userId);
     List<ProductRating> findByIdProductId(Long productId);
}
