package com.shop.clothingstore.service;

import com.shop.clothingstore.model.Product;
import com.shop.clothingstore.model.ProductSize;
import com.shop.clothingstore.requestModel.ProductSizeRequest;
import com.shop.clothingstore.requestModel.QuantityRequest;
import com.shop.clothingstore.repository.ProductRepository;
import com.shop.clothingstore.repository.ProductSizeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductSizeService {

    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;

    public ProductSizeService(ProductRepository productRepository, ProductSizeRepository productSizeRepository) {
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
    }

    public List<ProductSize> findAll(){
        return productSizeRepository.findAll();
    }

    public ProductSize create(Long productId, ProductSizeRequest request) {


        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого продукта нет"));


        ProductSize productSize = new ProductSize();
        productSize.setProduct(product);
        productSize.setSize(request.size());
        productSize.setQuantity(request.quantity());

        if(productSizeRepository.existsByProductIdAndSize(productId,request.size())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Такой товар уже есть");
        }

        return productSizeRepository.save(productSize);
    }

    public ProductSize updateQuantity(Long sizeId, QuantityRequest request){

        ProductSize productSize = productSizeRepository.findById(sizeId)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND,"Такого размера нет"));

        productSize.setQuantity(request.quantity());
        return productSizeRepository.save(productSize);
    }

    public List<ProductSize> getByProduct(Long productId){

        if(!productRepository.existsById(productId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Товар не найден");
        }

        return productSizeRepository.findByProductId(productId);
    }
}

