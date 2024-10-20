package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.ProductWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductWishlistRepository extends JpaRepository<ProductWishlist, Long>, JpaSpecificationExecutor<ProductWishlist> {

}
