package com.shop.clothingstore.requestModel;

import jakarta.validation.constraints.Positive;

public record QuantityRequest(
        @Positive int quantity) {
}
