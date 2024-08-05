package com.example.warehouse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SkuAllocationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void allocateSkuQuantity(String sku, int quantity) {
        String sql = "BEGIN allocate_sku_quantity(?, ?); END;";
        jdbcTemplate.update(sql, sku, quantity);
    }
}
