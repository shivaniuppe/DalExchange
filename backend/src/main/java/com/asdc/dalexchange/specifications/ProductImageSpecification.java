package com.asdc.dalexchange.specifications;

import com.asdc.dalexchange.model.ProductImage;
import org.springframework.data.jpa.domain.Specification;


public class ProductImageSpecification {

    public static Specification<ProductImage> byProductId(Long productId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("product").get("productId"), productId);
        };
    }
}
