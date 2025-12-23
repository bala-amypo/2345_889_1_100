package com.example.demo.controller;

import com.example.demo.model.DocumentType;
import com.example.demo.service.DocumentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-types")
@Tag(name = "Document Types")
@SecurityRequirement(name = "bearer-jwt")
public class DocumentTypeController {
    
    private final DocumentTypeService documentTypeService;
    
    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new document type")
    public ResponseEntity<DocumentType> createDocumentType(@RequestBody DocumentType documentType) {
        DocumentType createdType = documentTypeService.createDocumentType(documentType);
        return ResponseEntity.ok(createdType);
    }
    
    @GetMapping
    @Operation(summary = "Get all document types")
    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
        List<DocumentType> types = documentTypeService.getAllDocumentTypes();
        return ResponseEntity.ok(types);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get document type by ID")
    public ResponseEntity<DocumentType> getDocumentType(@PathVariable Long id) {
        DocumentType type = documentTypeService.getDocumentType(id);
        return ResponseEntity.ok(type);
    }
}