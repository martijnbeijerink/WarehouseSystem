package com.example.warehouse.GoodsToPicker.entity;

import jakarta.persistence.*;

@Entity
public class CartonItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "carton_id")
    private OutboundCarton carton;

    // Getter for sku
    public String getSku() {
        return sku;
    }

    // Other getters and setters
    public int getQuantity() {
        return quantity;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCarton(OutboundCarton carton) {
        this.carton = carton;
    }

    public OutboundCarton getCarton() {
        return carton;
    }
}
