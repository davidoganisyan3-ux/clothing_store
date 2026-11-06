package com.shop.clothingstore.model;

import jakarta.persistence.*;

@Entity
@Table(name="product_sizes")
public class ProductSize {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    private String size;
    private int quantity;
//геттеры
    public Long getId(){return id;}
    public Product getProduct(){return product;}
    public String getSize(){return size;}
    public int getQuantity(){return quantity;}
//сеттеры
    public void setProduct(Product product){
        this.product = product;
    }
    public void setSize(String size){this.size=size;}
    public void setQuantity(int quantity){this.quantity=quantity;}
}
