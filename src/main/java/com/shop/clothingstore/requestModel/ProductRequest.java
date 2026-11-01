package com.shop.clothingstore.requestModel;

import java.math.BigDecimal;

public record ProductRequest(String name, String description, BigDecimal price,Long categoryId)   {
}