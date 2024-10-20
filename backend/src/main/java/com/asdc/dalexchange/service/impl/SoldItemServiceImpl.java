package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.SoldItemDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.SoldItem;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.SoldItemRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.service.SoldItemService;
import com.asdc.dalexchange.specifications.SoldItemSpecification;
import com.asdc.dalexchange.util.AuthUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link SoldItemService} interface.
 * <p>
 * This service handles operations related to sold items, including retrieving all sold products for the current user
 * and saving a sold item based on the provided data.
 * </p>
 */
@Service
@AllArgsConstructor
@Slf4j
public class SoldItemServiceImpl implements SoldItemService {

    private final SoldItemRepository soldItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Mapper<SoldItem, SoldItemDTO> soldItemMapper;

    /**
     * Retrieves a list of {@link SoldItemDTO} representing all sold products for the current user.
     * <p>
     * This method fetches the user ID of the current user using {@link AuthUtil#getCurrentUserId(UserRepository)},
     * then retrieves all sold items associated with this user from the {@link SoldItemRepository}.
     * </p>
     *
     * @return a list of {@link SoldItemDTO} objects representing the sold products for the current user.
     */
    @Override
    public List<SoldItemDTO> GetAllSoldProduct() {
        Long userId = AuthUtil.getCurrentUserId(userRepository);
        log.info("Fetching all sold products for userId: {}", userId);

        List<SoldItem> allSoldItems = soldItemRepository.findAll(SoldItemSpecification.bySellerUserId(userId));
        List<SoldItemDTO> soldItemDTOs = allSoldItems.stream()
                .map(soldItemMapper::mapTo)
                .collect(Collectors.toList());

        log.info("Fetched {} sold products for userId: {}", soldItemDTOs.size(), userId);
        return soldItemDTOs;
    }

    /**
     * Saves a new sold item based on the provided request body.
     * <p>
     * This method extracts the product ID from the request body, checks if a sold item for this product already exists,
     * and if not, creates a new {@link SoldItem} entity with the current date and time, associating it with the product
     * and its seller. The new sold item is then saved in the {@link SoldItemRepository}.
     * </p>
     *
     * @param requestBody a {@link Map} containing the data for creating a sold item, including the "productId" key.
     * @throws RuntimeException if the product is not found, or if an error occurs while saving the sold item.
     */
    public void saveSoldItem(Map<String, Object> requestBody) {
        Long productId = Long.parseLong(requestBody.get("productId").toString());
        log.info("Attempting to save sold item for productId: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", productId);
                    return new RuntimeException("Product not found");
                });

        boolean exists = soldItemRepository.existsByProduct_ProductId(productId);
        if (exists) {
            log.info("SoldItem for productId {} already exists. Skipping save.", productId);
            return;
        }

        User seller = product.getSeller();
        SoldItem soldItem = new SoldItem();
        soldItem.setSeller(seller);
        soldItem.setProduct(product);
        soldItem.setSoldDate(LocalDateTime.now());

        try {
            soldItemRepository.save(soldItem);
            log.info("SoldItem successfully saved for productId: {}", productId);
        } catch (Exception e) {
            log.error("Error saving SoldItem for productId: {}. Error: {}", productId, e.getMessage());
            throw new RuntimeException("Error saving SoldItem", e);
        }
    }
}
