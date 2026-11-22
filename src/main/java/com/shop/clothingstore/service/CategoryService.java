package com.shop.clothingstore.service;

import com.shop.clothingstore.model.Category;
import com.shop.clothingstore.repository.CategoryRepository;
import com.shop.clothingstore.requestModel.CategoryRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;

    }

    public Category create(CategoryRequest request){

        Category category = new Category();
        category.setName(request.name());

       return  categoryRepository.save(category);
    }
}
