package com.example.warehouse.OutboundOrderManagement.outboundOrder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OutboundOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    private String Sku;
    private int quantity;

    public OutboundOrder() {
    }

    public OutboundOrder(String orderNumber, String Sku, int quantity) {
        this.orderNumber = orderNumber;
        this.Sku = Sku;
        this.quantity = quantity;
    }


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        this.Sku = Sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
