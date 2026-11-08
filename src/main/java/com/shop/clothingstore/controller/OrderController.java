package com.shop.clothingstore.controller;

import com.shop.clothingstore.model.Order;
import com.shop.clothingstore.requestModel.OrderRequest;
import com.shop.clothingstore.responseModel.OrderResponse;
import com.shop.clothingstore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

   private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }

    @PostMapping("/orders")
    public OrderResponse create(@Valid @RequestBody OrderRequest request){
       return orderService.created(request);
    }
}
