package com.example.warehouse.InboundOrderManagement.service;

import com.example.warehouse.InboundOrderManagement.inboundOrder.InboundOrder;
import com.example.warehouse.InboundOrderManagement.repository.InboundOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InboundOrderService {

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    public InboundOrder receiveInboundOrder(String sku, Integer quantity, String countryOfOrigin) {
        InboundOrder inboundOrder = new InboundOrder();
        inboundOrder.setSku(sku);
        inboundOrder.setQuantity(quantity);
        inboundOrder.setReceiveDate(LocalDate.now());  // Set the receive date to the current date
        inboundOrder.setCountryOfOrigin(countryOfOrigin);

        return inboundOrderRepository.save(inboundOrder);
    }
}
