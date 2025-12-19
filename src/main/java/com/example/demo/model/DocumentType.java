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

    @Column(unique = true, nullable = false)
    private String typeName;

    private String description;

    private Boolean required;

    private Integer weight;

    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "supportedDocumentTypes")
    private Set<Vendor> vendors = new HashSet<>();

    public DocumentType() {}

    public DocumentType(String typeName, String description, Boolean required, Integer weight) {
        this.typeName = typeName;
        this.description = description;
        this.required = required;
        this.weight = weight;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.weight == null || this.weight < 0) {
            this.weight = 0;
        }
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public Set<Vendor> getVendors() {
        return vendors;
    }
}