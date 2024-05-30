package com.example.warehouse.OutboundOrderManagement.service;

import com.example.warehouse.OutboundOrderManagement.outboundOrder.OutboundOrder;
import com.example.warehouse.OutboundOrderManagement.repository.OutboundOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OutboundOrderService {

    @Autowired
    private OutboundOrderRepository outboundOrderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public OutboundOrder saveOrder(OutboundOrder order) {
        // Check if SKU exists
        boolean skuExists = restTemplate.getForObject(
                "http://localhost:8080/api/skus/exists/" + order.getSku(), Boolean.class);

        if (!skuExists) {
            throw new RuntimeException("SKU does not exist");
        }

        return outboundOrderRepository.save(order);
    }
}
