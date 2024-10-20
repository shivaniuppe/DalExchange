package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.ProductListingDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductImage;
import com.asdc.dalexchange.repository.ProductImageRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.service.ProductListingService;
import com.asdc.dalexchange.specifications.ProductImageSpecification;
import com.asdc.dalexchange.specifications.ProductSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ProductListingService interface for managing product listings.
 */
@Service
@Slf4j
@AllArgsConstructor
public class ProductListingServiceImpl implements ProductListingService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final Mapper<Product, ProductListingDTO> productListingMapper;

    /**
     * Finds product listings based on the specified criteria.
     *
     * @param pageable   the pagination information.
     * @param search     the search query.
     * @param categories the list of categories to filter by.
     * @param conditions the list of conditions to filter by.
     * @param minPrice   the minimum price to filter by.
     * @param maxPrice   the maximum price to filter by.
     * @return a paginated list of ProductListingDTO objects.
     */
    @Override
    public Page<ProductListingDTO> findByCriteria(Pageable pageable, String search, List<String> categories, List<String> conditions, Double minPrice, Double maxPrice) {
        log.info("Find by Criteria call started in the ProductListingServiceImpl");
        Page<Product> productPage = getProductsPage(pageable, search, categories, conditions, minPrice, maxPrice);

        List<ProductListingDTO> productListingDTOs = getProductListingDTOs(productPage);

        return new PageImpl<>(productListingDTOs, pageable, productPage.getTotalElements());
    }

    /**
     * Maps a page of Product entities to a list of ProductListingDTOs.
     *
     * @param productPage the page of Product entities.
     * @return a list of ProductListingDTOs.
     */
    private List<ProductListingDTO> getProductListingDTOs(Page<Product> productPage) {
        log.debug("Mapping Product entities to ProductListingDTOs");
        return productPage.getContent().stream().map(product -> {
            ProductListingDTO productListingDTO = productListingMapper.mapTo(product);
            Specification<ProductImage> imageSpec = ProductImageSpecification.byProductId(productListingDTO.getProductId());
            List<ProductImage> productImages = productImageRepository.findAll(imageSpec);
            if (!productImages.isEmpty()) {
                productListingDTO.setImageUrl(productImages.get(0).getImageUrl());
                log.debug("Set image URL for product id: {}", productListingDTO.getProductId());
            }
            return productListingDTO;
        }).collect(Collectors.toList());
    }

    /**
     * Retrieves a paginated list of Product entities based on the specified criteria.
     *
     * @param pageable   the pagination information.
     * @param search     the search query.
     * @param categories the list of categories to filter by.
     * @param conditions the list of conditions to filter by.
     * @param minPrice   the minimum price to filter by.
     * @param maxPrice   the maximum price to filter by.
     * @return a paginated list of Product entities.
     */
    private Page<Product> getProductsPage(Pageable pageable, String search, List<String> categories, List<String> conditions, Double minPrice, Double maxPrice) {
        log.debug("Building specification for product search");
        Specification<Product> spec = Specification.where(ProductSpecification.hasTitleOrDescriptionContaining(search))
                .and(ProductSpecification.hasCategory(categories))
                .and(ProductSpecification.hasCondition(conditions))
                .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(ProductSpecification.isNotUnlisted());
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        log.debug("Retrieved {} products", productPage.getTotalElements());
        return productPage;
    }
}
