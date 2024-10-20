package com.asdc.dalexchange.model;

import jakarta.persistence.*;
import com.asdc.dalexchange.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "transaction_datetime")
    private LocalDateTime transactionDatetime;

    @OneToOne
    @JoinColumn(name = "shipping_address")
    private ShippingAddress shippingAddress;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "admin_comments")
    private String adminComments;
}
