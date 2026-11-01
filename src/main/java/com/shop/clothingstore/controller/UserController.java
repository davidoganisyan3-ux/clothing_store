package com.shop.clothingstore.controller;

import com.shop.clothingstore.model.User;
import com.shop.clothingstore.responseModel.UserResponse;
import com.shop.clothingstore.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController (UserService userService) {
         this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserResponse> getAll(){
       return  userService.getAll();
    }
}
