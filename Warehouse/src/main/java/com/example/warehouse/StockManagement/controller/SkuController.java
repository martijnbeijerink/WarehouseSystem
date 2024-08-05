package com.example.warehouse.StockManagement.controller;

import com.example.warehouse.StockManagement.repository.SkuRepository;
import com.example.warehouse.StockManagement.sku.Sku;
import com.example.warehouse.StockManagement.service.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/skus")
@Tag(name = "SKU Management", description = "APIs for managing SKUs")
public class SkuController {
    private static final Logger logger = LoggerFactory.getLogger(SkuController.class);
    @Autowired
    private SkuService skuService;

    @GetMapping
    @Operation(summary = "Get all SKUs")
    public List<Sku> getAllSkus() {
        return skuService.getAllSkus();
    }

    @PostMapping
    @Operation(summary = "Create a new SKU")
    public Sku createSku(@RequestBody Sku sku) {
        return skuService.saveSku(sku);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get SKU by Id")
    public Sku getSkuById(@PathVariable Long id) {
        return skuService.getSkuById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete SKU")
    public void deleteSku(@PathVariable Long id) {
        skuService.deleteSku(id);
    }

    @GetMapping("/exists/{sku}")
    @Operation(summary = "Check SKU exists")
    public ResponseEntity<Boolean> skuExists(@PathVariable String sku) {
        boolean exists = skuService.existsBySku(sku);
        return ResponseEntity.ok(exists);
    }
    // SKU UI
//    @GetMapping("/supervisor/skus")
//    public String listSkus(Model model) {
//        List<Sku> skus = skuService.getAllSkus();
//        model.addAttribute("skus", sku);
//        return "sku-list";  // Ensure this matches your template name
    }
