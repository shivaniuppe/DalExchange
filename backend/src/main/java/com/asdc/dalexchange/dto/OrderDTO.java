package com.asdc.dalexchange.dto;

import com.asdc.dalexchange.enums.OrderStatus;
import com.asdc.dalexchange.model.ShippingAddress;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long orderId;
    private String fullName;
    private String productTitle;
    private double totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime transactionDatetime;
    private ShippingAddress shippingAddress;
    private String adminComments;
}
