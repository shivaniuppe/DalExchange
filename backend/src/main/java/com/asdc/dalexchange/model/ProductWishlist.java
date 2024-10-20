package com.asdc.dalexchange.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_wishlist")
public class ProductWishlist{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long wishlistId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn (name = "product_id")
    private Product productId;
}
