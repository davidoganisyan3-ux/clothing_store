package com.shop.clothingstore.responseModel;

import java.time.LocalDateTime;
import java.util.List;

public record OrderHistoryResponse(Long id, String status, LocalDateTime createdAt, List<OrderItemResponse> items) {
}
