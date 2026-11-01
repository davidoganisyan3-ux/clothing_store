package com.shop.clothingstore.controller;

import com.shop.clothingstore.model.Category;
import com.shop.clothingstore.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import  org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }
}
