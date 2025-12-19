package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compliance_scores")
public class ComplianceScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Vendor vendor;

    private Double scoreValue;

    private String rating;

    private LocalDateTime lastEvaluated;

    public ComplianceScore() {}

    public ComplianceScore(Vendor vendor, Double scoreValue, String rating) {
        this.vendor = vendor;
        this.scoreValue = scoreValue;
        this.rating = rating;
        this.lastEvaluated = LocalDateTime.now();
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public Double getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Double scoreValue) {
        this.scoreValue = scoreValue;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setLastEvaluated(LocalDateTime lastEvaluated) {
        this.lastEvaluated = lastEvaluated;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}