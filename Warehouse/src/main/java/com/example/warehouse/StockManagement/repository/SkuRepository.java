package com.example.warehouse.StockManagement.repository;

import com.example.warehouse.StockManagement.sku.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {
    boolean existsBySku(String sku);}