package com.asdc.dalexchange.specifications;

import com.asdc.dalexchange.model.ProductWishlist;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ProductWishlistSpecification {

    public static Specification<ProductWishlist> byUserIdAndProductId(Long userId, Long productId) {
        return (Root<ProductWishlist> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate userPredicate = criteriaBuilder.equal(root.get("userId").get("id"), userId);
            Predicate productPredicate = criteriaBuilder.equal(root.get("productId").get("id"), productId);
            return criteriaBuilder.and(userPredicate, productPredicate);
        };
    }

    public static Specification<ProductWishlist> byUserId(Long userId) {
        return (Root<ProductWishlist> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId").get("id"), userId);
    }
}


