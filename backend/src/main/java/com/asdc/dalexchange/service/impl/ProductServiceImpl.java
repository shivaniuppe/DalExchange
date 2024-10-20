package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.AddProductDTO;
import com.asdc.dalexchange.dto.ProductDTO;
import com.asdc.dalexchange.dto.ProductModerationDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.ProductCategory;
import com.asdc.dalexchange.model.ProductImage;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.ProductCategoryRepository;
import com.asdc.dalexchange.repository.ProductImageRepository;
import com.asdc.dalexchange.repository.ProductRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.service.ProductService;
import com.asdc.dalexchange.util.AuthUtil;
import com.asdc.dalexchange.util.CloudinaryUtil;
import com.asdc.dalexchange.util.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the ProductService interface.
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private Mapper<Product, ProductDTO> productMapper;
    @Autowired
    private Mapper<Product, ProductModerationDTO> productModerationMapper;
    @Autowired
    private CloudinaryUtil cloudinaryUtil;

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the ProductDTO of the retrieved product
     */
    @Override
    public ProductDTO getProductById(Long productId) {
        log.info("Fetching product with ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        return productMapper.mapTo(product);
    }

    /**
     * Retrieves all products for moderation.
     *
     * @return a list of ProductModerationDTO objects
     */
    @Override
    public List<ProductModerationDTO> getAllProducts() {
        log.info("Fetching all products for moderation");
        return productRepository.findAll().stream()
                .map(productModerationMapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * Updates a product's details.
     *
     * @param productId the ID of the product to update
     * @param updatedProductDetails the updated product details
     * @return the updated ProductModerationDTO
     */
    @Override
    @Transactional
    public ProductModerationDTO updateProduct(Long productId, ProductModerationDTO updatedProductDetails) {
        log.info("Updating product with ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (updatedProductDetails.getTitle() != null) {
            product.setTitle(updatedProductDetails.getTitle());
        }
        if (updatedProductDetails.getDescription() != null) {
            product.setDescription(updatedProductDetails.getDescription());
        }
        if (updatedProductDetails.getPrice() != 0) {
            product.setPrice(updatedProductDetails.getPrice());
        }
        if (updatedProductDetails.getCategory() != null) {
            ProductCategory category = productCategoryRepository.findById(updatedProductDetails.getCategory().getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        if (updatedProductDetails.getProductCondition() != null) {
            product.setProductCondition(updatedProductDetails.getProductCondition());
        }
        if (updatedProductDetails.getUseDuration() != null) {
            product.setUseDuration(updatedProductDetails.getUseDuration());
        }
        if (updatedProductDetails.getShippingType() != null) {
            product.setShippingType(updatedProductDetails.getShippingType());
        }
        if (updatedProductDetails.getQuantityAvailable() != null) {
            product.setQuantityAvailable(updatedProductDetails.getQuantityAvailable());
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Product with ID: {} updated successfully", productId);
        return productModerationMapper.mapTo(updatedProduct);
    }

    /**
     * Unlists a product.
     *
     * @param productId the ID of the product to unlist
     * @param unlisted the unlisted status to set
     */
    @Override
    @Transactional
    public void unlistProduct(Long productId, boolean unlisted) {
        log.info("Unlisting product with ID: {}. Unlisted status: {}", productId, unlisted);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setUnlisted(unlisted);
        productRepository.save(product);
    }

    /**
     * Adds a new product along with its images.
     *
     * @param addProductDTO the DTO containing product details
     * @param category the category of the product
     * @param imageFiles the images of the product
     * @return the added product
     */
    @Override
    public Product addProduct(AddProductDTO addProductDTO, ProductCategory category, List<MultipartFile> imageFiles) {
        log.info("Adding new product with title: {}", addProductDTO.getTitle());

        // Get the current authenticated user
        User seller = AuthUtil.getCurrentUser(userRepository);

        // Create a new product and set its details
        Product product = new Product();
        product.setTitle(addProductDTO.getTitle());
        product.setDescription(addProductDTO.getDescription());
        product.setPrice(addProductDTO.getPrice());
        product.setCategory(category);
        product.setProductCondition(addProductDTO.getProductCondition());
        product.setUseDuration(addProductDTO.getUseDuration());
        product.setShippingType(addProductDTO.getShippingType());
        product.setQuantityAvailable(addProductDTO.getQuantityAvailable());
        product.setSeller(seller);
        product.setCreatedAt(LocalDateTime.now());

        // Save the product to the repository
        Product savedProduct = productRepository.save(product);
        log.info("Product with ID: {} added successfully", savedProduct.getProductId());

        // Upload images to Cloudinary and save their URLs to the product images
        List<ProductImage> images = imageFiles.stream().map(file -> {
            String imageUrl = cloudinaryUtil.uploadImage(file);
            ProductImage productImage = new ProductImage();
            productImage.setImageUrl(imageUrl);
            productImage.setProduct(savedProduct);
            return productImage;
        }).collect(Collectors.toList());

        // Save all product images to the repository
        productImageRepository.saveAll(images);
        log.info("Images for product with ID: {} uploaded and saved", savedProduct.getProductId());

        return savedProduct;
    }

    /**
     * Retrieves a product category by its ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the ProductCategory
     */
    @Override
    public ProductCategory getCategoryById(Long categoryId) {
        log.info("Fetching category with ID: {}", categoryId);
        return productCategoryRepository.findById(categoryId).orElse(null);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the Product
     */
    @Override
    public Product getProductByID(Long id) {
        log.info("Fetching product with ID: {}", id);
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a product for moderation by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the ProductModerationDTO
     */
    @Override
    public ProductModerationDTO getProductByIdForModeration(Long productId) {
        log.info("Fetching product for moderation with ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        return productModerationMapper.mapTo(product);
    }

    /**
     * Marks a product as sold based on the provided product ID.
     *
     * @param requestBody A map containing the product ID under the key "productId".
     * @throws RuntimeException If the product with the given ID is not found in the repository.
     */
    @Override
    public void markProductAsSold(Map<String, Object> requestBody) {
        Long productId = Long.parseLong(requestBody.get("productId").toString());
        log.info("Attempting to mark product as sold with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", productId);
                    return new RuntimeException("Product not found");
                });

        product.setSold(true);
        productRepository.save(product);

        log.info("Product with ID {} marked as sold successfully", productId);
    }

}
