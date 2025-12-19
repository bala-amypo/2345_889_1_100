package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_documents")
public class VendorDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Vendor vendor;

    @ManyToOne(optional = false)
    private DocumentType documentType;

    @Column(nullable = false)
    private String fileUrl;

    private LocalDateTime uploadedAt;

    private LocalDate expiryDate;

    private Boolean isValid;

    public VendorDocument() {}

    public VendorDocument(String fileUrl, LocalDate expiryDate) {
        this.fileUrl = fileUrl;
        this.expiryDate = expiryDate;
    }

    @PrePersist
    protected void onUpload() {
        this.uploadedAt = LocalDateTime.now();

        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            this.isValid = false;
        } else {
            this.isValid = true;
        }
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
}