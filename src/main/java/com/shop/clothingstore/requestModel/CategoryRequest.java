package com.shop.clothingstore.requestModel;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest (
        @NotBlank String name){
}
