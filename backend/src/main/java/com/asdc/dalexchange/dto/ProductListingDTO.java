package com.asdc.dalexchange.dto;

import com.asdc.dalexchange.enums.ProductCondition;
import com.asdc.dalexchange.enums.ShippingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListingDTO {
    private Long productId;
    private String title;
    private String description;
    private double price;
    private String categoryName;
    private ProductCondition productCondition;
    private String useDuration;
    private ShippingType shippingType;
    private Integer quantityAvailable;
    private String imageUrl;
    private LocalDateTime createdAt;
}
