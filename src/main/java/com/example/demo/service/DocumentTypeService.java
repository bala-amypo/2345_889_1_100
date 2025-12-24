package com.example.demo.service;

import com.example.demo.model.DocumentType;

import java.util.List;

public interface DocumentTypeService {

    DocumentType getDocumentType(Long id);

    List<DocumentType> getRequiredDocumentTypes();
}
