package com.example.warehouse.Supervisor;

import com.example.warehouse.OutboundOrderManagement.service.OutboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OutboundOrderControllerUI {

    @Autowired
    private OutboundOrderService outboundOrderService;

    @GetMapping("/supervisor/outbound-orders")
    public String listOutboundOrders(Model model) {
        model.addAttribute("orders", outboundOrderService.getAllOrders());
        return "outbound-orders";
    }
}
