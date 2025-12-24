package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compliance_rules")
public class ComplianceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;
    private String matchType;
    private Double threshold;
    private LocalDateTime createdAt;

    public ComplianceRule() {}

    @PrePersist
    public void prePersist() {
        if (this.threshold == null) {
            this.threshold = 0.0;
        }
        this.createdAt = LocalDateTime.now();
    }

    // ===== getters & setters =====

    public Long getId() {
        return id;
    }

    public String getRuleName() {
        return ruleName;
    }

    // used in tests
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getMatchType() {
        return matchType;
    }

    // used in tests
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public Double getThreshold() {
        return threshold;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
