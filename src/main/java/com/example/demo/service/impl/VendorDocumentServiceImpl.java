package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DocumentType;
import com.example.demo.model.Vendor;
import com.example.demo.model.VendorDocument;
import com.example.demo.repository.DocumentTypeRepository;
import com.example.demo.repository.VendorDocumentRepository;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.VendorDocumentService;

import java.time.LocalDate;

public class VendorDocumentServiceImpl implements VendorDocumentService {

    private final VendorDocumentRepository vendorDocumentRepository;
    private final VendorRepository vendorRepository;
    private final DocumentTypeRepository documentTypeRepository;

   
    public VendorDocumentServiceImpl(
            VendorDocumentRepository vendorDocumentRepository,
            VendorRepository vendorRepository,
            DocumentTypeRepository documentTypeRepository) {

        this.vendorDocumentRepository = vendorDocumentRepository;
        this.vendorRepository = vendorRepository;
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public VendorDocument uploadDocument(Long vendorId,
                                         Long documentTypeId,
                                         VendorDocument document) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vendor not found"));

        DocumentType documentType =
                documentTypeRepository.findById(documentTypeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("DocumentType not found"));

        if (document.getExpiryDate() != null &&
                document.getExpiryDate().isBefore(LocalDate.now())) {

            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }

        document.setVendor(vendor);
        document.setDocumentType(documentType);
        document.setIsValid(true);

        return vendorDocumentRepository.save(document);
    }

    @Override
    public VendorDocument getDocument(Long id) {
        return vendorDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("VendorDocument not found"));
    }
}
