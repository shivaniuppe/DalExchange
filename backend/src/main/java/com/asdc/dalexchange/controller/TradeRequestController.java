package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.TradeRequestDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Notification;
import com.asdc.dalexchange.model.TradeRequest;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.service.NotificationService;
import com.asdc.dalexchange.service.TradeRequestService;
import com.asdc.dalexchange.util.NotificationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing trade requests.
 */
@RestController
@Slf4j
@AllArgsConstructor
public class TradeRequestController {

    private TradeRequestService tradeRequestService;
    private NotificationService notificationService;
    private Mapper<TradeRequest, TradeRequestDTO> tradeRequestMapper;

    /**
     * Retrieve all sell trade requests.
     *
     * @return a list of TradeRequestDTO objects representing sell trade requests.
     */
    @GetMapping(path = "/sell_requests")
    public List<TradeRequestDTO> getSellRequests() {
        log.info("get sell_requests API endpoint called");
        List<TradeRequestDTO> sellRequests = tradeRequestService.getSellerTradeRequests();
        log.debug("Fetched {} sell requests", sellRequests.size());
        return sellRequests;
    }

    /**
     * Retrieve all buy trade requests.
     *
     * @return a list of TradeRequestDTO objects representing buy trade requests.
     */
    @GetMapping(path = "/buy_requests")
    public List<TradeRequestDTO> getBuyRequests() {
        log.info("get buy_requests API endpoint called");
        List<TradeRequestDTO> buyRequests = tradeRequestService.getBuyerTradeRequests();
        log.debug("Fetched {} buy requests", buyRequests.size());
        return buyRequests;
    }

    /**
     * Create a new trade request.
     *
     * @param requestBody the request body containing trade request data.
     * @return a ResponseEntity containing the created TradeRequestDTO object.
     */
    @PostMapping("/create_trade_request")
    public ResponseEntity<TradeRequestDTO> createTradeRequest(@RequestBody Map<String, Object> requestBody) {
        log.info("create_trade_request API endpoint called with requestBody: {}", requestBody);
        TradeRequestDTO createdTradeRequestDTO = tradeRequestService.createTradeRequest(requestBody);
        log.debug("Created trade request with id: {}", createdTradeRequestDTO.getRequestId());
        return new ResponseEntity<>(createdTradeRequestDTO, HttpStatus.CREATED);
    }

    /**
     * Update the status of an existing trade request.
     *
     * @param id     the ID of the trade request to update.
     * @param status the new status for the trade request.
     * @return a ResponseEntity containing the updated TradeRequestDTO object.
     */
    @PutMapping("/update_trade_status/{id}")
    public ResponseEntity<TradeRequestDTO> updateTradeRequestStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("updateTradeRequestStatus API endpoint called with id: {} and status: {}", id, status);
        TradeRequest updatedTradeRequest = tradeRequestService.updateTradeRequestStatus(id, status);
        TradeRequestDTO tradeRequestDTO = tradeRequestMapper.mapTo(updatedTradeRequest);
        log.debug("Updated trade request with id: {} to status: {}", id, status);
        sendNotification(updatedTradeRequest, status);
        return ResponseEntity.ok(tradeRequestDTO);
    }

    /**
     * Send notifications based on the status of the trade request.
     *
     * @param tradeRequest the trade request for which the notification is to be sent.
     * @param status       the status of the trade request.
     */
    private void sendNotification(TradeRequest tradeRequest, String status) {
        log.info("Sending notification for trade request id: {} with status: {}", tradeRequest.getRequestId(), status);
        User buyer = tradeRequest.getBuyer();
        User seller = tradeRequest.getSeller();
        Notification notification = new Notification();
        String title = NotificationUtil.getTitle(status);
        String message = NotificationUtil.getMessage(status, tradeRequest.getProduct().getTitle());

        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        notification.setUser(buyer);
        notification.setTitle(title);
        notification.setMessage(message);
        notificationService.sendNotification(notification);
        log.debug("Notification sent to buyer id: {} with title: {} and message: {}", buyer.getUserId(), title, message);

        if (status.equals("completed") || status.equals("canceled")) {
            String sellerTitle = NotificationUtil.getSellerTitle(status);
            String sellerMessage = NotificationUtil.getSellerMessage(status, tradeRequest.getProduct().getTitle());
            notification.setUser(seller);
            notification.setTitle(sellerTitle);
            notification.setMessage(sellerMessage);
            notificationService.sendNotification(notification);
            log.debug("Notification sent to seller id: {} with title: {} and message: {}", seller.getUserId(), sellerTitle, sellerMessage);
        }
    }
}
