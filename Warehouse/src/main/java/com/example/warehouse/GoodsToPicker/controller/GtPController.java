package com.example.warehouse.GoodsToPicker.controller;

import com.example.warehouse.GoodsToPicker.service.GtPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gtp")
public class GtPController {
    @Autowired
    private GtPService gtpService;

    @PostMapping("/process-order/{orderId}")
    public ResponseEntity<String> processOrder(@PathVariable Long orderId) {
        try {
            gtpService.processOutboundOrder(orderId);
            return ResponseEntity.ok("Order processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
