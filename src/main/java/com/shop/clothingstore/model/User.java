package com.shop.clothingstore.model;

import jakarta.persistence.*;

@Entity(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {return passwordHash;}

    public String getEmail() {return email;}

    public String getName() {return name;}

    public Long getId() {return id;}
}
