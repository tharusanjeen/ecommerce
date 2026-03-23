package com.project.ecommerce.service.image;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.project.ecommerce.dto.image.ProductImageRequest;
import com.project.ecommerce.dto.image.ProductImageResponse;

public interface ProductImageService {

    /**
     * Uploads multiple images for a product.
     * 
     * @param id      the product ID
     * @param request the image upload request containing image data
     * @return a list of responses for the uploaded images
     */
    List<ProductImageResponse> uploadImages(UUID id, ProductImageRequest request);

    /**
     * Retrieves all images associated with a specific product.
     * 
     * @param productId the product ID
     * @return a list of image upload responses for the product
     */
    List<ProductImageResponse> getImagesByProductId(UUID productId);

    /**
     * Updates primary image of a product and retrieves all images.
     * 
     * @param productId the product ID
     * @param imageId   the image ID
     * @return a list of images after updating primary image
     */
    List<ProductImageResponse> updatePrimaryImage(UUID productId, UUID imageId);

    /**
     * Deletes an image associated with a product.
     * 
     * @param productId the product ID
     * @param imageId   the image ID to be deleted
     * @return a map containing the result of the deletion operation
     */
    Map<String, Object> deleteImageById(UUID productId, UUID imageId);

    /**
     * 
     * @param productId the poduct ID
     * @return a map containing the result of the deletion operation
     */
    Map<String, Object> deleteAllImagesByProductId(UUID productId);
}