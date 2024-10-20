package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    /**
     * Find products by a list of product IDs.
     *
     * @param productIds the list of product IDs
     * @return a list of products with the given IDs
     */
    List<Product> findByProductIdIn(List<Long> productIds);
}
