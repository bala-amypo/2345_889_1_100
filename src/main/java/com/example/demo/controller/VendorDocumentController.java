package com.example.demo.controller;

import com.example.demo.model.VendorDocument;
import com.example.demo.service.VendorDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-documents")
@Tag(name = "Vendor Documents")
@SecurityRequirement(name = "bearer-jwt")
public class VendorDocumentController {
    
    private final VendorDocumentService vendorDocumentService;
    
    public VendorDocumentController(VendorDocumentService vendorDocumentService) {
        this.vendorDocumentService = vendorDocumentService;
    }
    
    @PostMapping
    @Operation(summary = "Upload a vendor document")
    public ResponseEntity<VendorDocument> uploadDocument(
            @RequestParam Long vendorId,
            @RequestParam Long typeId,
            @RequestBody VendorDocument document) {
        VendorDocument uploadedDocument = vendorDocumentService.uploadDocument(vendorId, typeId, document);
        return ResponseEntity.ok(uploadedDocument);
    }
    
    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get all documents for a vendor")
    public ResponseEntity<List<VendorDocument>> getDocumentsForVendor(@PathVariable Long vendorId) {
        List<VendorDocument> documents = vendorDocumentService.getDocumentsForVendor(vendorId);
        return ResponseEntity.ok(documents);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID")
    public ResponseEntity<VendorDocument> getDocument(@PathVariable Long id) {
        VendorDocument document = vendorDocumentService.getDocument(id);
        return ResponseEntity.ok(document);
    }
}