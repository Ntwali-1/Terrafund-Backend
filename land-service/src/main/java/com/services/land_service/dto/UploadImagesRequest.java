package com.services.land_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadImagesRequest {

    @NotNull(message = "Image URLs cannot be null")
    @NotEmpty(message = "At least one image URL is required")
    @Size(max = 10, message = "Cannot upload more than 10 images at once")
    private List<@URL(message = "Invalid image URL format") String> imageUrls;
}