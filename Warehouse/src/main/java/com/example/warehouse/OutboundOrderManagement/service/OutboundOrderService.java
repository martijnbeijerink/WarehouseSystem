package com.example.warehouse.OutboundOrderManagement.service;

import com.example.warehouse.OutboundOrderManagement.outboundOrder.OutboundOrder;
import com.example.warehouse.OutboundOrderManagement.repository.OutboundOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OutboundOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OutboundOrderService.class);

    @Autowired
    private OutboundOrderRepository outboundOrderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public OutboundOrder saveOrder(OutboundOrder order) {
        String skuCheckUrl = "http://localhost:8080/api/skus/exists/" + order.getSku();
        logger.info("Checking SKU existence with URL: {}", skuCheckUrl);

        ResponseEntity<Boolean> response = restTemplate.getForEntity(skuCheckUrl, Boolean.class);
        boolean skuExists = response.getBody();
        logger.info("SKU exists response: {}", skuExists);

        if (!skuExists) {
            throw new RuntimeException("SKU does not exist");
        }

        return outboundOrderRepository.save(order);
    }

    public Object getAllOrders() {
        return outboundOrderRepository.findAll();
    }
}
