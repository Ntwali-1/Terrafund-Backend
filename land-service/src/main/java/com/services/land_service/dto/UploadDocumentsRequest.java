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
public class UploadDocumentsRequest {

    @NotNull(message = "Document URLs cannot be null")
    @NotEmpty(message = "At least one document URL is required")
    @Size(max = 5, message = "Cannot upload more than 5 documents at once")
    private List<@URL(message = "Invalid document URL format") String> documentUrls;
}