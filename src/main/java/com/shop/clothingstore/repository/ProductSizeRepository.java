package com.shop.clothingstore.repository;

import com.shop.clothingstore.model.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductSizeRepository extends JpaRepository  <ProductSize,Long> {

    boolean existsByProductIdAndSize(Long productId,String size);

    List <ProductSize> findByProductId(Long productId);


}
