package com.shop.clothingstore.service;


import com.shop.clothingstore.model.Order;
import com.shop.clothingstore.model.OrderItem;
import com.shop.clothingstore.model.ProductSize;
import com.shop.clothingstore.model.User;
import com.shop.clothingstore.repository.*;
import com.shop.clothingstore.requestModel.OrderItemRequest;
import com.shop.clothingstore.requestModel.OrderRequest;
import com.shop.clothingstore.responseModel.OrderResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository ;
    private final  ProductSizeRepository productSizeRepository ;
    
    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,

                        UserRepository userRepository,
                        ProductSizeRepository productSizeRepository){
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productSizeRepository = productSizeRepository;
    }



    @Transactional
    public OrderResponse created(OrderRequest request){

        if(request.userId()==null||request.items()==null||request.items().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Пользователь или предметы отсутствуют");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException
                        (HttpStatus.NOT_FOUND,"Такого пользователя нет"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("NEW");
        order.setCreatedAt(LocalDateTime.now());
        Order saveOrder = orderRepository.save(order);

        for(OrderItemRequest itemRequest: request.items()){
            if(itemRequest==null||itemRequest.quantity()<0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Неверный ввод предмет");
            }

            ProductSize productSize = productSizeRepository.findById(itemRequest.productSizeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Такого размера нет"));

            if(productSize.getQuantity()< itemRequest.quantity()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"На складе нет столько товаров");
            }

            productSize.setQuantity(productSize.getQuantity() - itemRequest.quantity());
            productSizeRepository.save(productSize);

            OrderItem item = new OrderItem();
            item.setOrder(saveOrder);
            item.setPrice(productSize.getProduct().getPrice());
            item.setQuantity(itemRequest.quantity());
            item.setProductSize(productSize);
            orderItemRepository.save(item);

        }

        ;

        return new OrderResponse(saveOrder.getId(),user.getId(),saveOrder.getStatus(),saveOrder.getCreatedAt());
    }
}
