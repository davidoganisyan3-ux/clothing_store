package com.shop.clothingstore.controller;

import com.shop.clothingstore.responseModel.OrderHistoryResponse;
import com.shop.clothingstore.service.JwtService;
import com.shop.clothingstore.service.OrderHistoryService;
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

        Long currentUserId = (Long) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));


        if(!isAdmin&&!userId.equals(currentUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Нельзя смотреть чужие заказы");
        }

        return orderHistoryService.getHistory(userId);
    }


}
