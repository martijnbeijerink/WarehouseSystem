package com.example.warehouse.InboundOrderManagement.repository;

import com.example.warehouse.InboundOrderManagement.inboundOrder.InboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundOrderRepository extends JpaRepository<InboundOrder, Long> {
}
