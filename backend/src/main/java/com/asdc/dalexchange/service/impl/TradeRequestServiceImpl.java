package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.TradeRequestDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductImage;
import com.asdc.dalexchange.model.TradeRequest;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.ProductImageRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.TradeRequestRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.service.TradeRequestService;
import com.asdc.dalexchange.specifications.ProductImageSpecification;
import com.asdc.dalexchange.specifications.TradeRequestSpecification;
import com.asdc.dalexchange.util.AuthUtil;
import com.asdc.dalexchange.util.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Implementation of the TradeRequestService interface for managing trade requests.
 */
@Service
@Slf4j
@AllArgsConstructor
public class TradeRequestServiceImpl implements TradeRequestService {

    private final TradeRequestRepository tradeRequestRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;
    private final Mapper<TradeRequest, TradeRequestDTO> tradeRequestMapper;

    /**
     * Retrieves trade requests for the current user as a buyer.
     *
     * @return a list of TradeRequestDTOs representing buyer trade requests.
     */
    @Override
    public List<TradeRequestDTO> getBuyerTradeRequests() {
        log.info("getBuyerTradeRequests call started in the TradeRequestServiceImpl");
        Long buyerId = AuthUtil.getCurrentUserId(userRepository);
        Specification<TradeRequest> spec = TradeRequestSpecification.hasBuyerId(buyerId);
        List<TradeRequestDTO> tradeRequests = getTradeRequests(spec);
        log.debug("Fetched {} trade requests for all buyers", tradeRequests.size());
        return tradeRequests;
    }

    /**
     * Retrieves trade requests for the current user as a seller.
     *
     * @return a list of TradeRequestDTOs representing seller trade requests.
     */
    @Override
    public List<TradeRequestDTO> getSellerTradeRequests() {
        log.info("getSellerTradeRequests call started in the TradeRequestServiceImpl");
        Long sellerId = AuthUtil.getCurrentUserId(userRepository);
        Specification<TradeRequest> spec = TradeRequestSpecification.hasSellerId(sellerId);
        List<TradeRequestDTO> tradeRequests = getTradeRequests(spec);
        log.debug("Fetched {} seller trade requests", tradeRequests.size());
        return tradeRequests;
    }

    /**
     * Updates the status of a trade request.
     *
     * @param requestId the ID of the trade request to update.
     * @param status    the new status for the trade request.
     * @return the updated TradeRequest.
     */
    @Override
    public TradeRequest updateTradeRequestStatus(Long requestId, String status) {
        log.info("updateTradeRequestStatus call started in the TradeRequestServiceImpl with requestId: {} and status: {}", requestId, status);
        TradeRequest tradeRequest = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TradeRequest id: " + requestId));

        tradeRequest.setRequestStatus(status);
        TradeRequest updatedTradeRequest = tradeRequestRepository.save(tradeRequest);
        log.debug("Updated trade request with id: {} to status: {}", requestId, status);
        return updatedTradeRequest;
    }

    /**
     * Creates a new trade request.
     *
     * @param requestBody the request body containing trade request data.
     * @return the created TradeRequestDTO.
     */
    @Override
    public TradeRequestDTO createTradeRequest(Map<String, Object> requestBody) {
        log.info("createTradeRequest call started in the TradeRequestServiceImpl with data: {}", requestBody);

        Long productId = Long.valueOf(requestBody.get("productId").toString());
        Long sellerId = Long.valueOf(requestBody.get("sellerId").toString());
        Long buyerId = AuthUtil.getCurrentUserId(userRepository);
        double requestedPrice = Double.parseDouble(requestBody.get("requestedPrice").toString());

        TradeRequest tradeRequest = new TradeRequest();
        Product product = findProductById(productId);
        User seller = findUserById(sellerId);
        User buyer = findUserById(buyerId);

        tradeRequest.setProduct(product);
        tradeRequest.setSeller(seller);
        tradeRequest.setBuyer(buyer);
        tradeRequest.setRequestedPrice(requestedPrice);
        tradeRequest.setRequestStatus("pending");
        tradeRequest.setRequestedAt(LocalDateTime.now());

        TradeRequest createdTradeRequest = tradeRequestRepository.save(tradeRequest);
        TradeRequestDTO tradeRequestDTO = tradeRequestMapper.mapTo(createdTradeRequest);
        log.debug("Created trade request with id: {}", createdTradeRequest.getRequestId());
        return tradeRequestDTO;
    }

    /**
     * Retrieves trade requests based on the given specification.
     *
     * @param spec the specification to filter trade requests.
     * @return a list of TradeRequestDTOs representing the filtered trade requests.
     */
    private List<TradeRequestDTO> getTradeRequests(Specification<TradeRequest> spec) {
        log.info("Fetching trade requests with specification: {}", spec);
        List<TradeRequest> tradeRequests = tradeRequestRepository.findAll(spec);
        List<TradeRequestDTO> tradeRequestDTOs = tradeRequests.stream()
                .map(tradeRequestMapper::mapTo)
                .toList();
        tradeRequestDTOs.forEach(tradeRequestDTO -> {
            long productId = tradeRequestDTO.getProduct().getProductId();
            Specification<ProductImage> imageSpec = ProductImageSpecification.byProductId(productId);
            List<ProductImage> productImages = productImageRepository.findAll(imageSpec);
            if (!productImages.isEmpty()) {
                tradeRequestDTO.getProduct().setImageUrl(productImages.get(0).getImageUrl());
                log.debug("Set image URL for product id: {}", productId);
            }
        });
        log.debug("Fetched {} all trade requests", tradeRequestDTOs.size());
        return tradeRequestDTOs;
    }

    /**
     * Finds a product by its ID.
     *
     * @param productId the ID of the product to find.
     * @return the found Product.
     * @throws ResourceNotFoundException if the product is not found.
     */
    private Product findProductById(Long productId) {
        log.info("Finding product by id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        log.debug("Found product with id: {}", productId);
        return product;
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId the ID of the user to find.
     * @return the found User.
     * @throws ResourceNotFoundException if the user is not found.
     */
    private User findUserById(Long userId) {
        log.info("Finding user by id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        log.debug("Found user with id: {}", userId);
        return user;
    }

    /**
     * Retrieves the approved trade request amount for a given product.
     *
     * @param productId the ID of the product.
     * @return the approved trade request amount.
     */
    @Override
    public double getApprovedTradeRequestAmount(Long productId) {
        log.info("getApprovedTradeRequestAmount call started in the TradeRequestServiceImpl with productId: {}", productId);
        Long userId = AuthUtil.getCurrentUserId(userRepository);
        List<TradeRequest> tradeRequests = tradeRequestRepository.findAll(
                where(TradeRequestSpecification.hasBuyerId(userId))
                        .and(TradeRequestSpecification.hasProductId(productId))
                        .and(TradeRequestSpecification.hasRequestStatus("approved")));
        if (tradeRequests.isEmpty()) {
            log.warn("No approved trade requests found for productId: {} and userId: {}", productId, userId);
            throw new ResourceNotFoundException("Approved trade request not found for productId: " + productId + " and userId: " + userId);
        }
        TradeRequest firstTradeRequest = tradeRequests.get(0);
        log.debug("Found approved trade request with id: {}", firstTradeRequest.getRequestId());
        return firstTradeRequest.getRequestedPrice();
    }

    /**
     * Updates the status of trade requests by product.
     *
     * @param requestBody the request body containing the product ID and other necessary information.
     * @return a message indicating the result of the operation.
     */
    @Override
    public String updateStatusByProduct(Map<String, Object> requestBody) {
        log.info("updateStatusByProduct call started in the TradeRequestServiceImpl with data: {}", requestBody);
        Long productId = Long.parseLong(requestBody.get("productId").toString());
        Long userId = AuthUtil.getCurrentUserId(userRepository);
        Specification<TradeRequest> specification = Specification
                .where(TradeRequestSpecification.hasProductId(productId))
                .and(TradeRequestSpecification.hasBuyerId(userId)
                .and(TradeRequestSpecification.hasRequestStatus("approved")));

        TradeRequest tradeRequest = tradeRequestRepository.findOne(specification).orElse(null);
        log.debug("Found trade request for productId: {} and userId: {}", productId, userId);
        if (tradeRequest != null) {
            tradeRequest.setRequestStatus("completed");
            tradeRequestRepository.save(tradeRequest);
            log.debug("Updated trade request status to completed for id: {}", tradeRequest.getRequestId());
            return "Trade request updated successfully";
        } else {
            log.warn("TradeRequest not found for productId: {} and userId: {}", productId, userId);
            return "TradeRequest not found for product ID: " + productId + " and user ID: " + userId;
        }
    }
}
