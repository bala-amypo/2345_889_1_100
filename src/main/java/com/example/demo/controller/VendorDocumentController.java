package com.example.demo.controller;

import com.example.demo.model.VendorDocument;
import com.example.demo.service.VendorDocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor-documents")
public class VendorDocumentController {

    private final VendorDocumentService vendorDocumentService;

    public VendorDocumentController(VendorDocumentService vendorDocumentService) {
        this.vendorDocumentService = vendorDocumentService;
    }

    @PostMapping
    public VendorDocument create(@RequestBody VendorDocument document) {
        return vendorDocumentService.create(document);
    }

    @GetMapping("/{id}")
    public VendorDocument get(@PathVariable Long id) {
        return vendorDocumentService.getById(id);
    }

    @GetMapping
    public List<VendorDocument> getAll() {
        return vendorDocumentService.getAll();
    }

    @PutMapping("/{id}")
    public VendorDocument update(@PathVariable Long id,
                                 @RequestBody VendorDocument document) {
        return vendorDocumentService.update(id, document);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vendorDocumentService.delete(id);
    }
}