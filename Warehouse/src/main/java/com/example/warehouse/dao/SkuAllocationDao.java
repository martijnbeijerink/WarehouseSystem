package com.example.warehouse.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class SkuAllocationDao {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public void allocateSkuQuantity(Long orderId, String sku, int quantity) {
        String procedureCall = "{call allocate_sku_quantity(?, ?, ?)}";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            callableStatement.setLong(1, orderId);
            callableStatement.setString(2, sku);
            callableStatement.setInt(3, quantity);

            callableStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error allocating SKU quantity", e);
        }
    }
}
