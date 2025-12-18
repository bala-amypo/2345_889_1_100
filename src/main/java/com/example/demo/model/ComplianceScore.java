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
    private LocalDateTime lastEvaluated;
    private String rating;

    public ComplianceScore() {
    }

    public ComplianceScore(Long id, Vendor vendor, Double scoreValue,
                           LocalDateTime lastEvaluated, String rating) {
        this.id = id;
        this.vendor = vendor;
        this.scoreValue = scoreValue;
        this.lastEvaluated = lastEvaluated;
        this.rating = rating;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public Double getScoreValue() { return scoreValue; }
    public void setScoreValue(Double scoreValue) { this.scoreValue = scoreValue; }

    public LocalDateTime getLastEvaluated() { return lastEvaluated; }
    public void setLastEvaluated(LocalDateTime lastEvaluated) { this.lastEvaluated = lastEvaluated; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
}