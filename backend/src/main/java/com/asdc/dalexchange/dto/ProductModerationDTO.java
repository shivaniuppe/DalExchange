package com.asdc.dalexchange.dto;

import com.asdc.dalexchange.enums.ProductCondition;
import com.asdc.dalexchange.enums.ShippingType;
import com.asdc.dalexchange.model.ProductCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductModerationDTO {
    private Long productId;
    private String title;
    private String description;
    private double price;
    private ProductCategory category;
    private ProductCondition productCondition;
    private String useDuration;
    private ShippingType shippingType;
    private Integer quantityAvailable;
    private LocalDateTime createdAt;
    private boolean unlisted;
}
