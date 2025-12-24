package com.example.demo.controller;

import com.example.demo.model.DocumentType;
import com.example.demo.service.DocumentTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-types")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @PostMapping
    public DocumentType create(@RequestBody DocumentType type) {
        return documentTypeService.createDocumentType(type);
    }

    @GetMapping
    public List<DocumentType> getAll() {
        return documentTypeService.getAllDocumentTypes();
    }

    @GetMapping("/{id}")
    public DocumentType get(@PathVariable Long id) {
        return documentTypeService.getDocumentType(id);
    }
}
