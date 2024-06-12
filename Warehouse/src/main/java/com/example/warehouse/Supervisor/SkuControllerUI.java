package com.example.warehouse.Supervisor.controller;

import com.example.warehouse.StockManagement.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SkuControllerUI {

    @Autowired
    private SkuService skuService;

    @GetMapping("/supervisor/skus")
    public String listSkus(Model model) {
        model.addAttribute("skus", skuService.getAllSkus());
        return "skus";
    }
}
