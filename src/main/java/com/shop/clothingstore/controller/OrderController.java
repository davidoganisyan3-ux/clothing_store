package com.shop.clothingstore.controller;

import com.shop.clothingstore.model.Order;
import com.shop.clothingstore.requestModel.OrderRequest;
import com.shop.clothingstore.responseModel.OrderResponse;
import com.shop.clothingstore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OrderController {

   private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }

    @PostMapping("/orders")
    public OrderResponse create(@Valid @RequestBody OrderRequest request,
                                Authentication authentication){
        Long requestUsId = request.userId();

        Long currentUserId = (Long) authentication.getPrincipal();

        if(!requestUsId.equals(currentUserId)){
            throw new ResponseStatusException
                    (HttpStatus.FORBIDDEN,"Нельзя сделать заказ за другого человека");
        }

       return orderService.created(request);

    }
}
