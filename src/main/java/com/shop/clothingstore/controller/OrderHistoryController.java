package com.shop.clothingstore.controller;

import com.shop.clothingstore.responseModel.OrderHistoryResponse;
import com.shop.clothingstore.service.OrderHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderHistoryController {

    private OrderHistoryService orderHistoryService;

    public OrderHistoryController(OrderHistoryService orderHistoryService){
        this.orderHistoryService=orderHistoryService;
    }

    @GetMapping("/user/{userId}/orders")
    public List<OrderHistoryResponse> getHistory(@PathVariable Long userId){
        return orderHistoryService.getHistory(userId);
    }


}
