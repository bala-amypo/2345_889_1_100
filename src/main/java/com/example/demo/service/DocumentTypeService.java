package com.example.demo.service;

import com.example.demo.model.DocumentType;
import java.util.List;

public interface DocumentTypeService {
    DocumentType create(DocumentType type);
    List<DocumentType> getAll();
    void delete(Long id);
}