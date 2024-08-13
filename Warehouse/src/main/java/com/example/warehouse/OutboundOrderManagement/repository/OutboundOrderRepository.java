package com.example.warehouse.OutboundOrderManagement.repository;

import com.example.warehouse.OutboundOrderManagement.outboundOrder.OutboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OutboundOrderRepository extends JpaRepository<OutboundOrder, Long> {
    Optional<OutboundOrder> findByOrderNumber(String orderNumber);
}
