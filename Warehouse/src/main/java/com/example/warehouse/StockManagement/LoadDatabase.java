package com.example.warehouse.StockManagement;

import com.example.warehouse.StockManagement.sku.Sku;
import com.example.warehouse.StockManagement.repository.SkuRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(SkuRepository repository) {
        return args -> {
            repository.save(new Sku("SKU001", "Sample SKU 1", "Description 1", 100));
            repository.save(new Sku("SKU002", "Sample SKU 2", "Description 2", 200));
        };
    }
}
