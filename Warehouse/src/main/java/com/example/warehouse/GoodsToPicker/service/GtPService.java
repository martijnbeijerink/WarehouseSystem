package com.example.warehouse.GoodsToPicker.service;

import com.example.warehouse.GoodsToPicker.controller.CartonItemRepository;
import com.example.warehouse.GoodsToPicker.controller.OutboundCartonRepository;
import com.example.warehouse.GoodsToPicker.entity.CartonItem;
import com.example.warehouse.GoodsToPicker.entity.OutboundCarton;
import com.example.warehouse.GoodsToPicker.exception.OrderNotFoundException;
import com.example.warehouse.OutboundOrderManagement.outboundOrder.OutboundOrder;
import com.example.warehouse.OutboundOrderManagement.repository.OutboundOrderRepository;
import com.example.warehouse.StockManagement.repository.SkuRepository;
import com.example.warehouse.StockManagement.sku.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class GtPService {
    @Autowired
    private OutboundCartonRepository cartonRepository;

    @Autowired
    private CartonItemRepository itemRepository;

    @Autowired
    private OutboundOrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    public void processOutboundOrder(Long orderId) {
        OutboundOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OutboundCarton carton = new OutboundCarton();
        OutboundCarton newCarton = carton.copy(UUID.randomUUID().toString());
        newCarton.setStatus("IN_PROGRESS");
        newCarton.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        newCarton = cartonRepository.save(newCarton);

        // Iterate over the items in the order
        for (CartonItem orderItem : order.getItems()) {
            // Check if SKU exists and has enough quantity
            Sku sku = skuRepository.findBySku(orderItem.getSku());
            if (sku != null && sku.getQuantity() >= orderItem.getQuantity()) {
                // Create a new CartonItem and save it
                CartonItem cartonItem = new CartonItem();
                cartonItem.setSku(orderItem.getSku());
                cartonItem.setQuantity(orderItem.getQuantity());
                cartonItem.setCarton(carton);
                itemRepository.save(cartonItem);

                // Decrease SKU quantity
                sku.setQuantity(sku.getQuantity() - orderItem.getQuantity());
                skuRepository.save(sku);
            } else {
                throw new OrderNotFoundException.InsufficientStockException(orderItem.getSku());
            }
        }

        newCarton.setStatus("COMPLETED");
        newCarton.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        cartonRepository.save(newCarton);

        order.setStatus("PICKED");
        orderRepository.save(order);
    }
}