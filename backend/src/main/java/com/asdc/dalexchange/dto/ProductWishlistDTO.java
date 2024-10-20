package com.asdc.dalexchange.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWishlistDTO {
    private long userId;
    private long productId;
}
