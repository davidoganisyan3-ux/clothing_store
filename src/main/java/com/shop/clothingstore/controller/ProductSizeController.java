package com.shop.clothingstore.controller;


import com.shop.clothingstore.model.ProductSize;
import com.shop.clothingstore.requestModel.ProductSizeRequest;
import com.shop.clothingstore.requestModel.QuantityRequest;
import com.shop.clothingstore.service.ProductSizeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductSizeController {

   private final ProductSizeService productSizeService;

    public ProductSizeController(ProductSizeService productSizeService) {
        this.productSizeService =  productSizeService;
    }
        @GetMapping("/product_sizes")
        public List<ProductSize> getAll(){
            return productSizeService.findAll();
        }

        @GetMapping("/product/{productId}/sizes")
        public List<ProductSize> getByProduct(@PathVariable Long productId){
         return productSizeService.getByProduct(productId);
        }

        @PostMapping("/products/{productId}/sizes")
           public ProductSize create(@PathVariable Long productId,
                   @RequestBody ProductSizeRequest request){
                return productSizeService.create(productId,request);
        }

        @PatchMapping("/product_sizes/{sizeId}")
    public ProductSize updateQuantity(@PathVariable Long sizeId, @RequestBody QuantityRequest request){
        return productSizeService.updateQuantity(sizeId,request);
        }


    

}
