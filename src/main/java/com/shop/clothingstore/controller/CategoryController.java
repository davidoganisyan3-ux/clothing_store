package com.shop.clothingstore.controller;

import com.shop.clothingstore.model.Category;
import com.shop.clothingstore.repository.CategoryRepository;
import com.shop.clothingstore.requestModel.CategoryRequest;
import com.shop.clothingstore.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import  org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public CategoryController(
            CategoryRepository categoryRepository,
            CategoryService categoryService){
        this.categoryRepository = categoryRepository;
        this.categoryService=categoryService;
    }

    @GetMapping("/categories")
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    @PostMapping("/categories/create")
    public Category create(@Valid @RequestBody CategoryRequest request){
       return categoryService.create(request);
    }
}
