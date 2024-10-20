package com.asdc.dalexchange.model;

import com.asdc.dalexchange.enums.ProductCondition;
import com.asdc.dalexchange.enums.ShippingType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_condition", nullable = false)
    private ProductCondition productCondition;

    @Column(name = "use_duration", nullable = false)
    private String useDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_type", columnDefinition = "VARCHAR(255) DEFAULT 'free'")
    private ShippingType shippingType;

    @Column(name = "quantity_available", columnDefinition = "INT DEFAULT 1")
    private Integer quantityAvailable;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "unlisted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean unlisted;

    @Column(name = "is_sold", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isSold;
}
