package com.asdc.dalexchange.specifications;

import com.asdc.dalexchange.model.TradeRequest;
import org.springframework.data.jpa.domain.Specification;

public class TradeRequestSpecification {

    public static Specification<TradeRequest> hasBuyerId(Long buyerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("buyer").get("userId"), buyerId);
    }

    public static Specification<TradeRequest> hasSellerId(Long sellerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("seller").get("userId"), sellerId);
    }

    public static Specification<TradeRequest> hasProductId(Long productId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("product").get("id"), productId);
    }

    public static Specification<TradeRequest> hasRequestStatus(String requestStatus) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("requestStatus"), requestStatus);
    }
}
