package com.shop.clothingstore.responseModel;

import java.time.LocalDateTime;

public record OrderResponse(Long id, Long userId, String status, LocalDateTime createdAt) {
}
