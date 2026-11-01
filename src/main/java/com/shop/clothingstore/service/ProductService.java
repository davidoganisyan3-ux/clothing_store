package com.shop.clothingstore.service;

import com.shop.clothingstore.model.Category;
import com.shop.clothingstore.model.Product;
import com.shop.clothingstore.requestModel.ProductRequest;
import com.shop.clothingstore.repository.CategoryRepository;
import com.shop.clothingstore.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository =  categoryRepository;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product create( ProductRequest request){
        if(request.name()==null||request.name().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Название не может быть пустым");
        }
        if(request.price()==null||request.price().compareTo(BigDecimal.ZERO)<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Цена должна быть больше нуля");
        }
        if(request.categoryId()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Должна быть категория");
        }
        Category category = categoryRepository.findById(request.categoryId()).orElse(null);
        if(category==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Такой категории нет");
        }
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);

        return productRepository.save(product);
    }


}
