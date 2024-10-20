package com.asdc.dalexchange.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRatingDTO {
    private String title;
    private String review;
    private Integer rating;
}
