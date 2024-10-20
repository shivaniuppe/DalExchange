package com.asdc.dalexchange.dto;

import com.asdc.dalexchange.enums.ProductCondition;
import com.asdc.dalexchange.enums.ShippingType;
import com.asdc.dalexchange.model.ProductCategory;
import com.asdc.dalexchange.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductDTO {
    private Long productId;
    private User seller;
    private String title;
    private String description;
    private double price;
    private ProductCategory category;
    private ProductCondition productCondition;
    private String useDuration;
    private ShippingType shippingType;
    private Integer quantityAvailable;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}
