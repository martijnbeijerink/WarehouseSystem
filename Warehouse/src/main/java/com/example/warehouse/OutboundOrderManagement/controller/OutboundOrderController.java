package com.example.warehouse.OutboundOrderManagement.controller;

import com.example.warehouse.OutboundOrderManagement.outboundOrder.OutboundOrder;
import com.example.warehouse.OutboundOrderManagement.service.OutboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/outbound-orders")
@Tag(name = "Outbound Order Management", description = "APIs for managing Outbound Orders")
public class OutboundOrderController {

    @Autowired
    private OutboundOrderService service;

    @PostMapping
    @Operation(summary = "Create a new Outbound Order")
    public ResponseEntity<OutboundOrder> createOrder(@RequestBody OutboundOrder order) {
        OutboundOrder savedOrder = service.saveOrder(order); // Use the instance method on the service instance
        return ResponseEntity.ok(savedOrder);
    }
}
