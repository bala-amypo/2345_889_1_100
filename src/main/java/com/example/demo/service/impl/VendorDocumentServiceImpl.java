package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.VendorDocument;
import com.example.demo.repository.DocumentTypeRepository;
import com.example.demo.repository.VendorDocumentRepository;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.VendorDocumentService;
import org.springframework.stereotype.Service;
import java.util.List;

import java.time.LocalDate;

@Service
public class VendorDocumentServiceImpl implements VendorDocumentService {

    private final VendorDocumentRepository vendorDocumentRepository;
    private final VendorRepository vendorRepository;
    private final DocumentTypeRepository documentTypeRepository;

    // ✅ EXACT constructor order (important for tests)
    public VendorDocumentServiceImpl(
            VendorDocumentRepository vendorDocumentRepository,
            VendorRepository vendorRepository,
            DocumentTypeRepository documentTypeRepository) {

        this.vendorDocumentRepository = vendorDocumentRepository;
        this.vendorRepository = vendorRepository;
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public VendorDocument create(VendorDocument document) {

        if (document.getExpiryDate() != null &&
            document.getExpiryDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Document has expired");
        }

        document.setIsValid(true);
        return vendorDocumentRepository.save(document);
    }

    @Override
    public VendorDocument getById(Long id) {
        return vendorDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    @Override
    public List<VendorDocument> getAll() {
        return vendorDocumentRepository.findAll();
    }

    @Override
    public VendorDocument update(Long id, VendorDocument updated) {
        VendorDocument existing = getById(id);
        existing.setFileUrl(updated.getFileUrl());
        existing.setExpiryDate(updated.getExpiryDate());
        return vendorDocumentRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        vendorDocumentRepository.delete(getById(id));
    }
}