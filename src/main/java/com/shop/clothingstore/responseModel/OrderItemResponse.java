package com.shop.clothingstore.responseModel;

import java.math.BigDecimal;

public record OrderItemResponse(Long id, String productName, String size, int quantity, BigDecimal price) {
}
