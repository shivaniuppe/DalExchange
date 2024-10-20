package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.*;
import com.asdc.dalexchange.service.ProductRatingService;
import com.asdc.dalexchange.service.ProductWishlistService;
import com.asdc.dalexchange.service.ProfilePageService;
import com.asdc.dalexchange.service.SoldItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user profile operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Slf4j
public class ProfilePageController {

    private final ProfilePageService profilePageService;
    private final ProductRatingService productRatingService;
    private final SoldItemService soldItemService;
    private final ProductWishlistService productWishlistService;

    /**
     * Retrieves the profile details for the currently logged-in user.
     *
     * @return a {@link ResponseEntity} containing the {@link EditProfileDTO} with user profile details.
     */
    @GetMapping("")
    public ResponseEntity<EditProfileDTO> profileDetails() {
        log.info("Fetching profile details");
        EditProfileDTO updatedUser = profilePageService.editGetUserDetails();
        log.info("Profile details fetched successfully");
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Retrieves a list of all saved products for the user.
     *
     * @return a {@link ResponseEntity} containing a list of {@link SavedProductDTO} objects representing saved products.
     */
    @GetMapping("/saved_products")
    public ResponseEntity<List<SavedProductDTO>> getAllSavedProducts() {
        log.info("Fetching all saved products");
        List<SavedProductDTO> savedProductDTOs = productWishlistService.getAllSavedProducts();
        log.info("Fetched {} saved products", savedProductDTOs.size());
        return ResponseEntity.ok(savedProductDTOs);
    }

    /**
     * Retrieves a list of all sold products for the user.
     *
     * @return a {@link ResponseEntity} containing a list of {@link SoldItemDTO} objects representing sold products.
     */
    @GetMapping("/sold_products")
    public ResponseEntity<List<SoldItemDTO>> getAllSoldProducts() {
        log.info("Fetching all sold products");
        List<SoldItemDTO> soldProductDTOs = soldItemService.GetAllSoldProduct();
        log.info("Fetched {} sold products", soldProductDTOs.size());
        return ResponseEntity.ok(soldProductDTOs);
    }

    /**
     * Retrieves a list of all purchased products for the user.
     *
     * @return a {@link ResponseEntity} containing a list of {@link PurchaseProductDTO} objects representing purchased products.
     */
    @GetMapping("/purchased_products")
    public ResponseEntity<List<PurchaseProductDTO>> getAllPurchasedProducts() {
        log.info("Fetching all purchased products");
        List<PurchaseProductDTO> purchasedProductDTOs = productWishlistService.getAllPurchasedProduct();
        log.info("Fetched {} purchased products", purchasedProductDTOs.size());
        return ResponseEntity.ok(purchasedProductDTOs);
    }

    /**
     * Retrieves a list of all product ratings given by the user.
     *
     * @return a {@link ResponseEntity} containing a list of {@link ProductRatingDTO} objects representing product ratings.
     */
    @GetMapping("/product_ratings")
    public ResponseEntity<List<ProductRatingDTO>> getAllProductRatings() {
        log.info("Fetching all product ratings");
        List<ProductRatingDTO> productRatingDTOs = productRatingService.allReviewOfAllSoldItemsOfUser();
        log.info("Fetched {} product ratings", productRatingDTOs.size());
        return ResponseEntity.ok(productRatingDTOs);
    }

    /**
     * Updates user details based on provided {@link EditProfileDTO} information.
     *
     * @param editProfileDTO the {@link EditProfileDTO} object containing updated user details.
     * @return a {@link ResponseEntity} containing the updated {@link EditProfileDTO}.
     */
    @PutMapping("/edit_user")
    public ResponseEntity<EditProfileDTO> editUserDetails(@RequestBody EditProfileDTO editProfileDTO) {
        log.info("Updating user details for userId: {}", editProfileDTO.getUserId());
        EditProfileDTO updatedUser = profilePageService.editUserDetails(editProfileDTO);
        log.info("User details updated successfully for userId: {}", editProfileDTO.getUserId());
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Removes a product from the user's list of favorites based on the provided product ID.
     *
     * @param productId the ID of the product to be removed from favorites.
     * @return a {@link ResponseEntity} with a message indicating the result of the operation.
     */
    @PutMapping("/remove_saved/{productId}")
    public ResponseEntity<Object> unmarkAsFavorite(@PathVariable long productId) {
        log.info("Unmarking productId: {} as favorite", productId);
        boolean isUpdated = productWishlistService.markProductAsFavorite(productId);
        if (isUpdated) {
            log.info("ProductId: {} removed from favorites successfully", productId);
            return ResponseEntity.ok("Product removed successfully");
        } else {
            log.info("ProductId: {} was not in favorites", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in favorites");
        }
    }
}
