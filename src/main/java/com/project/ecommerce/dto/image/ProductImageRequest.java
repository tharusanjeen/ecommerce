package com.project.ecommerce.dto.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageRequest {
    @NotNull(message = "Images are required!")
    @Size(min = 3, max = 10, message = "You can upload 3 to 10 images only")
    private List<@NotNull MultipartFile> images;
}
