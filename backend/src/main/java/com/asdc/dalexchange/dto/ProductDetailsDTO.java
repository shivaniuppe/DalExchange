package com.asdc.dalexchange.dto;

import com.asdc.dalexchange.enums.ProductCondition;
import com.asdc.dalexchange.enums.ShippingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDTO {
    private Long productId;
    private String title;
    private String description;
    private double price;
    private String category;
    private ProductCondition productCondition;
    private String useDuration;
    private ShippingType shippingType;
    private Integer quantityAvailable;
    private List<String> imageUrls;
    private boolean  Favorite;
    private Long SellerId;
    private String SellerName;
    private double Rating;
    private LocalDateTime sellerJoiningDate;
    private boolean isSold;
}
