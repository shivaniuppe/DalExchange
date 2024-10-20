package com.asdc.dalexchange.service;

import com.asdc.dalexchange.dto.TradeRequestDTO;
import com.asdc.dalexchange.model.TradeRequest;

import java.util.List;
import java.util.Map;

public interface TradeRequestService {
    List<TradeRequestDTO> getBuyerTradeRequests();
    List<TradeRequestDTO> getSellerTradeRequests();
    TradeRequest updateTradeRequestStatus(Long requestId, String status);
    TradeRequestDTO createTradeRequest(Map<String, Object> requestBody);
    double getApprovedTradeRequestAmount(Long productId);
    String updateStatusByProduct(Map<String, Object> requestBody);
}
