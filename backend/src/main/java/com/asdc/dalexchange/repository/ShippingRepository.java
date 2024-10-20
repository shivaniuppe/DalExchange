package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingAddress, Long> {
}