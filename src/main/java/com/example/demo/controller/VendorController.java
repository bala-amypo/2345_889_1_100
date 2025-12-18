package com.example.demo.controller;

import com.example.demo.model.Vendor;
import com.example.demo.service.VendorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public Vendor create(@RequestBody Vendor vendor) {
        return vendorService.create(vendor);
    }

    @GetMapping("/{id}")
    public Vendor get(@PathVariable Long id) {
        return vendorService.getById(id);
    }

    @GetMapping
    public List<Vendor> getAll() {
        return vendorService.getAll();
    }

    @PutMapping("/{id}")
    public Vendor update(@PathVariable Long id, @RequestBody Vendor vendor) {
        return vendorService.update(id, vendor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vendorService.delete(id);
    }
}