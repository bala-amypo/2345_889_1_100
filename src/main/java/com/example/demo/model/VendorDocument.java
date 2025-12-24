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

    @ManyToOne
    private Vendor vendor;

    @ManyToOne
    private DocumentType documentType;

    private String fileUrl;
    private LocalDate expiryDate;
    private Boolean isValid;
    private LocalDateTime uploadedAt;

    public VendorDocument() {}

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }

    // ===== getters & setters =====

    public Long getId() {
        return id;
    }

    // used in tests
    public void setId(Long id) {
        this.id = id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    // used in tests
    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    // used in tests
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    // used in tests
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    // used in tests
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    // used in tests
    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
