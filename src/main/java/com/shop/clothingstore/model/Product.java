package com.shop.clothingstore.model;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name="products")
public class Product {


    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    private String name;
    private String disciption;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
//геттеры
    public Long getId(){return id;}
    public String getName(){return name;}
    public String getDescription(){return disciption;}
    public BigDecimal getPrice(){return price;}
    public Category getCategory(){return category;}
//сеттеры
    public void setName(String name){
        this.name=name;
    }
    public void setDescription(String description){this.disciption=description;}
    public void setPrice(BigDecimal price){
        this.price=price;
    }
    public void setCategory(Category category){
        this.category=category;
    }


}
