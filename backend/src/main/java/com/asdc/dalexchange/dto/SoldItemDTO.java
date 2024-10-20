package com.asdc.dalexchange.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoldItemDTO {
    private Long soldItemId;
    private String title;
    private Double price;
    private LocalDateTime soldDate;
}
