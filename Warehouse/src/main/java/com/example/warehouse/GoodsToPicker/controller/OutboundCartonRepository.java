package com.example.warehouse.GoodsToPicker.controller;

import com.example.warehouse.GoodsToPicker.entity.OutboundCarton;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboundCartonRepository extends JpaRepository<OutboundCarton, Long> {
    List<OutboundCarton> findByStatus(String status);
}
