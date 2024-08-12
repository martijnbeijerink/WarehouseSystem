package com.example.warehouse.GoodsToPicker.exception;

public class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderNotFoundException(Long orderId) {
        super("Order with ID " + orderId + " not found");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public static class InsufficientStockException extends RuntimeException {
        public InsufficientStockException(String sku) {
            super("Insufficient stock for SKU: " + sku);
        }
    }
}
