package com.asdc.dalexchange.specifications;

import com.asdc.dalexchange.model.Payment;
import org.springframework.data.jpa.domain.Specification;

public class PaymentSpecification {

    public static Specification<Payment> hasOrderId(Long orderId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("order").get("orderId"),
                orderId
        );
    }

    public static Specification<Payment> hasPaymentId(Long paymentId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("paymentId"),
                paymentId
        );
    }
}

