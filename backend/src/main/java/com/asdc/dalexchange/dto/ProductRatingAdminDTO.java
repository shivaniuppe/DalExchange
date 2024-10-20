package com.asdc.dalexchange.dto;

import lombok.Data;

@Data
public class ProductRatingAdminDTO {
    private Long productId;
    private Long userId;
    private String review;
    private Integer rating;
}
