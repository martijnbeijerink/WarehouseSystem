package com.example.warehouse.OutboundOrderManagement.service;

import com.example.warehouse.OutboundOrderManagement.outboundOrder.OutboundOrder;
import com.example.warehouse.OutboundOrderManagement.repository.OutboundOrderRepository;
import com.example.warehouse.dao.SkuAllocationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class OutboundOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OutboundOrderService.class);

    @Autowired
    private OutboundOrderRepository outboundOrderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SkuAllocationDao skuAllocationDao;

    public ResponseEntity<?> saveOrder(OutboundOrder order) {
        // Check if an order with the same orderNumber already exists
        Optional<OutboundOrder> existingOrder = outboundOrderRepository.findByOrderNumber(order.getOrderNumber());
        if (existingOrder.isPresent()) {
            String errorMessage = "Order with order number " + order.getOrderNumber() + " already exists.";
            logger.warn(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        String skuCheckUrl = "http://localhost:8080/api/skus/exists/" + order.getSku();
        logger.info("Checking SKU existence with URL: {}", skuCheckUrl);

        ResponseEntity<Boolean> response = restTemplate.getForEntity(skuCheckUrl, Boolean.class);
        logger.info("Response from SKU existence check: {}", response);

        Boolean skuExists = response.getBody();
        logger.info("SKU exists response: {}", skuExists);

        if (skuExists == null || !skuExists) {
            String errorMessage = "SKU does not exist.";
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        // Save the order since it doesn't exist and SKU is valid
        OutboundOrder savedOrder = outboundOrderRepository.save(order);

        // Allocate SKU quantity and update order status to 'IN-PROGRESS'
        skuAllocationDao.allocateSkuQuantity(savedOrder.getOrderNumber());

        // Return the saved order wrapped in a ResponseEntity
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    public Object getAllOrders() {
        return outboundOrderRepository.findAll();
    }
}
