package com.shop.clothingstore.service;


import com.shop.clothingstore.model.Order;
import com.shop.clothingstore.model.OrderItem;
import com.shop.clothingstore.repository.OrderItemRepository;
import com.shop.clothingstore.repository.OrderRepository;
import com.shop.clothingstore.repository.UserRepository;
import com.shop.clothingstore.responseModel.OrderHistoryResponse;
import com.shop.clothingstore.responseModel.OrderItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderHistoryService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;



    public OrderHistoryService(OrderItemRepository orderItemRepository,
                               OrderRepository orderRepository,
                               UserRepository userRepository
                              ){
        this.orderItemRepository=orderItemRepository;
        this.orderRepository=orderRepository;
        this.userRepository=userRepository;
    }

    public List<OrderHistoryResponse> getHistory(Long userId) {

        if(!userRepository.existsById(userId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Такого пользователя нет");
        }

        List<OrderHistoryResponse> result = new ArrayList<>();

        for (Order order : orderRepository.findByUserId(userId)) {

            List<OrderItemResponse> items = new ArrayList<>();

            for (OrderItem item : orderItemRepository.findByOrderId(order.getId())) {
                items.add(new OrderItemResponse(
                        item.getId(),
                        item.getProductSize().getProduct().getName(),
                        item.getProductSize().getSize(),
                        item.getQuantity(),
                        item.getPrice()
                ));
            }
            result.add(new OrderHistoryResponse(
                    order.getId(),
                    order.getStatus(),
                    order.getCreatedAt(),
                    items
            ));
        }
                return result;
    }

}
