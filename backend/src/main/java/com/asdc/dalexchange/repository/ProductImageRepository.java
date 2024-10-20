package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long>, JpaSpecificationExecutor<ProductImage> {

}
