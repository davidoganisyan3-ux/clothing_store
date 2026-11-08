package com.shop.clothingstore.controller;

import com.shop.clothingstore.requestModel.ProductRequest;

import com.shop.clothingstore.service.ProductService;
import com.shop.clothingstore.model.Product;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import  org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getAll(){
        return  productService.getAll();
    }

    @PostMapping("/products")
    public Product create(@Valid  @RequestBody ProductRequest request){
        return productService.create(request);
    }
}
