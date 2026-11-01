package com.shop.clothingstore.service;

import com.shop.clothingstore.model.User;
import com.shop.clothingstore.repository.UserRepository;
import com.shop.clothingstore.responseModel.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

   private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAll(){

        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail()))
                .toList();
    }



}
