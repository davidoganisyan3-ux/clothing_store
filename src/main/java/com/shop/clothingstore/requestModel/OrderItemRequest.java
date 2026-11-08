package com.shop.clothingstore.requestModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        @NotNull @Positive Long productSizeId,
        @Positive int quantity){
}
