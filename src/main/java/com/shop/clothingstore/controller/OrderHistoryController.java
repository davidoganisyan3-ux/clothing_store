package com.shop.clothingstore.controller;

import com.shop.clothingstore.responseModel.OrderHistoryResponse;
import com.shop.clothingstore.service.JwtService;
import com.shop.clothingstore.service.OrderHistoryService;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;


@RestController
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;
    private final JwtService jwtService;

    public OrderHistoryController(OrderHistoryService orderHistoryService,
                                  JwtService jwtService){
        this.orderHistoryService=orderHistoryService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user/{userId}/orders")
    public List<OrderHistoryResponse> getHistory(@PathVariable Long userId,
                                                 Authentication authentication)  {

        Long currentUserTd = (Long) authentication.getPrincipal();


        if(!userId.equals(currentUserTd)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Нельзя смотреть чужие заказы");
        }

        return orderHistoryService.getHistory(userId);
    }


}
