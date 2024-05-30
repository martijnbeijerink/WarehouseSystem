package com.example.warehouse.StockManagement.service;

import com.example.warehouse.StockManagement.repository.SkuRepository;
import com.example.warehouse.StockManagement.sku.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {
    @Autowired
    private SkuRepository skuRepository;

    public List<Sku> getAllSkus() {
        return skuRepository.findAll();
    }

    public Sku saveSku(Sku sku) {
        return skuRepository.save(sku);
    }

    public Sku getSkuById(Long id) {
        return skuRepository.findById(id).orElse(null);
    }

    public void deleteSku(Long id) {
        skuRepository.deleteById(id);
    }

    public boolean existsByCode(String code) {
        return skuRepository.existsByCode(code);
    }
}
