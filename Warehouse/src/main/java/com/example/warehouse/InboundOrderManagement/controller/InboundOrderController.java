package com.example.warehouse.InboundOrderManagement.controller;


import com.example.warehouse.InboundOrderManagement.inboundOrder.InboundOrder;
import com.example.warehouse.InboundOrderManagement.service.InboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inbound-orders")
public class InboundOrderController {

    @Autowired
    private InboundOrderService inboundOrderService;

    @PostMapping("/receive")
    public InboundOrder receiveInboundOrder(@RequestParam String sku,
                                            @RequestParam Integer quantity,
                                            @RequestParam String countryOfOrigin) {
        return inboundOrderService.receiveInboundOrder(sku, quantity, countryOfOrigin);
    }
}
