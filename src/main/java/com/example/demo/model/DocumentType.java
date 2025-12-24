package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "document_types")
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean required;
    private int weight;
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "supportedDocumentTypes")
    private Set<Vendor> vendors = new HashSet<>();

    public DocumentType() {}

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

    public boolean getRequired() {
        return required;
    }

    // used in tests
    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getWeight() {
        return weight;
    }

    // used in tests
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<Vendor> getVendors() {
        return vendors;
    }
}
