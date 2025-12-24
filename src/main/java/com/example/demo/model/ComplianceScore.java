package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compliance_scores")
public class ComplianceScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Vendor vendor;

    private Double scoreValue;
    private String rating;
    private LocalDateTime lastEvaluated;

    public ComplianceScore() {}

    // ===== getters & setters =====

    public Long getId() {
        return id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    // used in tests
    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Double getScoreValue() {
        return scoreValue;
    }

    // used in tests
    public void setScoreValue(Double scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getRating() {
        return rating;
    }

    // used in tests
    public void setRating(String rating) {
        this.rating = rating;
    }

    public LocalDateTime getLastEvaluated() {
        return lastEvaluated;
    }

    public void setLastEvaluated(LocalDateTime lastEvaluated) {
        this.lastEvaluated = lastEvaluated;
    }
}
