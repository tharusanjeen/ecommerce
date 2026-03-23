package com.project.ecommerce.service.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.ecommerce.dto.image.ProductImageRequest;
import com.project.ecommerce.dto.image.ProductImageResponse;
import com.project.ecommerce.exception.CustomExceptions.BadRequestException;
import com.project.ecommerce.exception.CustomExceptions.ImageNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.ProductNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.RequestEntityTooLargeException;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.ProductImage;
import com.project.ecommerce.repository.ProductImageRepository;
import com.project.ecommerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;

    private static final String UPLOAD_DIR = "uploads";
    private static final String IMAGE_DIR = "product_images";

    private static final int MAX_IMAGES_PER_PRODUCT = 10;
    private static final long MAX_SIZE_OF_IMAGE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public List<ProductImageResponse> uploadImages(UUID productId, ProductImageRequest request){

        // 1 Validate product
        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // 2 Validate number of images
        int imagesCount = imageRepository.countByProductId(productId);
        if(imagesCount + request.getImages().size() > MAX_IMAGES_PER_PRODUCT) {
            throw new BadRequestException("Exceeds max allowed images per product");
        }

        // 3 validate each file : not empty, size, contentType
        for(MultipartFile file : request.getImages()) {
            if(file.isEmpty()) throw new BadRequestException("File cannot be empty!");
            if(!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) throw new BadRequestException("Unsupported file type"); 
            if(file.getSize() > MAX_SIZE_OF_IMAGE) throw new RequestEntityTooLargeException("File exceeds size limit");
        }

        // 4 Create the upload dir if it doesn't exist
        Path imageUploadPath = Paths.get(UPLOAD_DIR, IMAGE_DIR);
        File uploadDir = imageUploadPath.toFile();
        
        if(!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 5 upload images and create ProductImage entities
        List<ProductImageResponse> uploadResponse = new ArrayList<>();

        for(MultipartFile file : request.getImages()) {

            if(file.isEmpty()) continue;

            try {
                String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

                if (extension == null) {
                    throw new BadRequestException("Invalid file extension");
                }

                String uniqueFileName = "product-" + product.getName().replaceAll("[^A-Za-z0-9,_-]", "_") + "-" + UUID.randomUUID() + "." + extension;

                File destinationFile = imageUploadPath.resolve(uniqueFileName).toFile();
                Files.copy(file.getInputStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                boolean hasPrimary = imageRepository.existsByProductIdAndIsPrimaryTrue(productId);

                ProductImage image = ProductImage
                        .builder()
                        .filename(uniqueFileName)
                        .filePath(destinationFile.toString())
                        .altText(product.getName())
                        .product(product)
                        .isPrimary(!hasPrimary)
                        .build();

                uploadResponse.add(ProductImageResponse.from(imageRepository.save(image), baseUrl));

            } catch (IOException e) {
                throw new RuntimeException("Error uploading image : " + file.getOriginalFilename() + "->" + e.getMessage(), e);
            }
        }

        return uploadResponse;
    }

    @Override
    public List<ProductImageResponse> getImagesByProductId(UUID productId) {
        
        List<ProductImageResponse> images = imageRepository.findByProductId(productId).stream().map(image -> ProductImageResponse.from(image, baseUrl)).collect(Collectors.toList());

        if(images.isEmpty()) {
            throw new ImageNotFoundException("Images not found!");
        }

        return images;
    }

    @Override
    public Map<String, Object> deleteImageById(UUID productId, UUID imageId) {

        ProductImage image = imageRepository.findByIdAndProductId(imageId, productId)
        .orElseThrow(() -> new ImageNotFoundException("Image not found"));

        try {
            Path filePath = Paths.get(image.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file: " + image.getFilePath(), e);
        }

        boolean wasPrimary = image.isPrimary();
        
        imageRepository.delete(image);

        if(wasPrimary) {
            Optional<ProductImage> another = imageRepository.findFirstByProductId(productId);

            another.ifPresent(img -> {
                img.setPrimary(true);
                imageRepository.save(img);
            });
        }


        return Map.of("success", "true", "message", "Image deleted with id " + image.getId());
    }

    @Override
    public Map<String, Object> deleteAllImagesByProductId(UUID productId) {

        List<ProductImage> images = imageRepository.findByProductId(productId);

        if(images.isEmpty()) {
            throw new ImageNotFoundException("Images not found!");
        }

        for(ProductImage image : images) {

            try {
                Path filePath = Paths.get(image.getFilePath());
                Files.deleteIfExists(filePath);
                
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image file: " + image.getFilePath(), e);
            }

            imageRepository.delete(image);

        }

        return Map.of("success", true, "message", "All images delete of product : " + productId);
    }

    @Override
    public List<ProductImageResponse> updatePrimaryImage(UUID productId, UUID imageId) {

        Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found!"));

        ProductImage newPrimary = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException("Image not found"));

        for(ProductImage image : product.getImages()) {

            if(image.isPrimary()) {
                image.setPrimary(false);
            }
        }

        newPrimary.setPrimary(true);

        imageRepository.saveAll(product.getImages());

        return product.getImages().stream().map(img -> ProductImageResponse.from(img, baseUrl)).collect(Collectors.toList());

    }
}
