package com.asdc.dalexchange.dto;

import com.asdc.dalexchange.enums.ProductCondition;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavedProductDTO {
    private Long productId;
    private String title;
    private double price;
    private String category;
    private ProductCondition productCondition;
    private String useDuration;
    private Integer quantityAvailable;
}