package com.shop.clothingstore.requestModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderRequest(
        @NotNull @Positive Long userId,
       @NotEmpty @Valid List<OrderItemRequest> items) {
}
