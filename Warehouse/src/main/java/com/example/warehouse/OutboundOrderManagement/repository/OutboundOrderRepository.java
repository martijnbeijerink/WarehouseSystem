package com.example.warehouse.OutboundOrderManagement.repository;

import com.example.warehouse.OutboundOrderManagement.outboundOrder.OutboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundOrderRepository extends JpaRepository<OutboundOrder, Long> {
}
