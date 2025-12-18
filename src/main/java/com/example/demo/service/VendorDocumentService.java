package com.example.demo.service;

import com.example.demo.model.VendorDocument;
import java.util.List;

public interface VendorDocumentService {
    VendorDocument create(VendorDocument document);
    VendorDocument getById(Long id);
    List<VendorDocument> getAll();
    VendorDocument update(Long id, VendorDocument document);
    void delete(Long id);
}