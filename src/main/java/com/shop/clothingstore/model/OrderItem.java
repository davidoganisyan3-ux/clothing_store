package com.shop.clothingstore.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_size_id")
    private ProductSize productSize;

    private int quantity;

    private BigDecimal price;
//геттеры
    public long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public ProductSize getProductSize() {
        return productSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
//сеттеры
    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
