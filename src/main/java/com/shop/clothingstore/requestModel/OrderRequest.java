package com.shop.clothingstore.requestModel;

import java.util.List;

public record OrderRequest(Long userId, List<OrderItemRequest> items) {
}
