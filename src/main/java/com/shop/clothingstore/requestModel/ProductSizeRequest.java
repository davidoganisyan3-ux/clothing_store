package com.shop.clothingstore.requestModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductSizeRequest(
        @NotBlank String size,
        @Positive int quantity) {
}
