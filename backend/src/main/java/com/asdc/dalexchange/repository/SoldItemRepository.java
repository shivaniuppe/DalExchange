package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.SoldItem;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SoldItemRepository extends JpaRepository<SoldItem, Integer>, JpaSpecificationExecutor<SoldItem> {
    List<SoldItem> findAll(Specification<SoldItem> specification);
    boolean existsByProduct_ProductId(Long productId);

}