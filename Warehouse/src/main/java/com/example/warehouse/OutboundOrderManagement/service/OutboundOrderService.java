package com.example.warehouse.OutboundOrderManagement.service;

import com.example.warehouse.OutboundOrderManagement.outboundOrder.OutboundOrder;
import com.example.warehouse.OutboundOrderManagement.repository.OutboundOrderRepository;
import com.example.warehouse.dao.SkuAllocationDao;
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

    @Autowired
    private SkuAllocationDao skuAllocationDao;

    public OutboundOrder saveOrder(OutboundOrder order) {
        String skuCheckUrl = "http://localhost:8080/api/skus/exists/" + order.getSku();
        logger.info("Checking SKU existence with URL: {}", skuCheckUrl);

        ResponseEntity<Boolean> response = restTemplate.getForEntity(skuCheckUrl, Boolean.class);
        logger.info("Response from SKU existence check: {}", response);

        Boolean skuExists = response.getBody();
        logger.info("SKU exists response: {}", skuExists);

        if (skuExists == null || !skuExists) {
            throw new RuntimeException("SKU does not exist");
        }
        // Allocate SKU quantity using the DAO
        skuAllocationDao.allocateSkuQuantity(order.getSku(), order.getQuantity());
        return outboundOrderRepository.save(order);
    }

    public Object getAllOrders() {
        return outboundOrderRepository.findAll();
    }
}
