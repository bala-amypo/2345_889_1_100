package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

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

    @Column(nullable = false)
    private String fileUrl;

    private LocalDate uploadedAt;
    private LocalDate expiryDate;
    private Boolean isValid;

    public VendorDocument() {
    }

    public VendorDocument(Long id, Vendor vendor, DocumentType documentType, String fileUrl,
                          LocalDate uploadedAt, LocalDate expiryDate, Boolean isValid) {
        this.id = id;
        this.vendor = vendor;
        this.documentType = documentType;
        this.fileUrl = fileUrl;
        this.uploadedAt = uploadedAt;
        this.expiryDate = expiryDate;
        this.isValid = isValid;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public LocalDate getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDate uploadedAt) { this.uploadedAt = uploadedAt; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Boolean getIsValid() { return isValid; }
    public void setIsValid(Boolean isValid) { this.isValid = isValid; }
}