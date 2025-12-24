package com.example.demo.service;

import com.example.demo.model.DocumentType;
import java.util.List;

public interface DocumentTypeService {

    DocumentType createDocumentType(DocumentType type);

    List<DocumentType> getAllDocumentTypes();

    DocumentType getDocumentType(Long id);
}
