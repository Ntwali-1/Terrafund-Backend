package com.services.investment_service.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignContractRequest {

    @NotBlank(message = "Contract URL is required")
    private String contractUrl;

    @NotNull(message = "Agreement confirmation is required")
    private Boolean agreedToTerms;
}
