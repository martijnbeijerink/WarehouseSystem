package com.example.warehouse.GoodsToPicker.controller;

import com.example.warehouse.GoodsToPicker.entity.CartonItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartonItemRepository extends JpaRepository<CartonItem, Long> {
}
