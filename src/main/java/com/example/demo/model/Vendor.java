package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendorName;
    private String industry;
    private LocalDateTime createdAt;

    @ManyToMany
    private Set<DocumentType> supportedDocumentTypes = new HashSet<>();

    public Vendor() {}

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ===== getters & setters =====

    public Long getId() {
        return id;
    }

    // used in tests
    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorName() {
        return vendorName;
    }

    // used in tests
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getIndustry() {
        return industry;
    }

    // used in tests
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // used heavily in tests
    public Set<DocumentType> getSupportedDocumentTypes() {
        return supportedDocumentTypes;
    }
}
