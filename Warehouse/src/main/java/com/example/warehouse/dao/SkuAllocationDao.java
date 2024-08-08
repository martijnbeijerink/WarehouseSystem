package com.example.warehouse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SkuAllocationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void allocateSkuQuantity(long orderId, String sku, int quantity) {
        jdbcTemplate.update(
                "CALL allocate_sku_quantity(?, ?, ?)",
                orderId, sku, quantity
        );
    }
}
