package com.asdc.dalexchange.specifications;

import com.asdc.dalexchange.model.SoldItem;
import org.springframework.data.jpa.domain.Specification;

public class SoldItemSpecification {

    public static Specification<SoldItem> bySellerUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("product").get("seller").get("userId"), userId);
        };
    }
}
