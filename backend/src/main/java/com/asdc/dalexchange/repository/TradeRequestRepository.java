package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long>, JpaSpecificationExecutor<TradeRequest> {

}
