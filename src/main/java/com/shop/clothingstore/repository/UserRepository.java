package com.shop.clothingstore.repository;

import com.shop.clothingstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    public boolean existsByEmail(String email);
}
