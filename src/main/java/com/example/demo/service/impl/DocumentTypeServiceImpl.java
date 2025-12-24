package com.example.demo.service.impl;

import com.example.demo.model.DocumentType;
import com.example.demo.repository.DocumentTypeRepository;
import com.example.demo.service.DocumentTypeService;

import java.util.List;

public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public DocumentType getDocumentType(Long id) {
        return documentTypeRepository.findById(id).orElse(null);
    }

    @Override
    public List<DocumentType> getRequiredDocumentTypes() {
        return documentTypeRepository.findByRequiredTrue();
    }
}
