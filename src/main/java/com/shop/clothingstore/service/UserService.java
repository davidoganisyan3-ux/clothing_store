package com.shop.clothingstore.service;

import com.shop.clothingstore.model.User;
import com.shop.clothingstore.repository.UserRepository;
import com.shop.clothingstore.requestModel.LoginRequest;
import com.shop.clothingstore.requestModel.UserRequest;
import com.shop.clothingstore.responseModel.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

   private final UserRepository userRepository;

   private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponse register(UserRequest request){

        if(userRepository.existsByEmail(request.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Такой email уже существует");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(encoder.encode(request.password()));

        userRepository.save(user);

        return new UserResponse(user.getId(),user.getName(),user.getEmail());

    }


    public UserResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new ResponseStatusException
                        (HttpStatus.UNAUTHORIZED,"Неверный логин или пароль"));

        if (!encoder.matches(request.password(),user.getPasswordHash())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Неверный логин или пароль");
        }

        return new UserResponse(user.getId(),user.getName(),user.getEmail());




    }


    public List<UserResponse> getAll(){

        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail()))
                .toList();
    }




}
