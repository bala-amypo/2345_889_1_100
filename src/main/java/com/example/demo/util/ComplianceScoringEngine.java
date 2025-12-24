package com.example.demo.util;

import com.example.demo.model.DocumentType;

import java.util.List;

public class ComplianceScoringEngine {

  
    public double calculateScore(List<DocumentType> required,
                                 List<DocumentType> submitted) {

        // Edge case tested explicitly
        if (required == null || required.isEmpty()) {
            return 100.0;
        }

        int totalWeight = required.stream()
                .mapToInt(DocumentType::getWeight)
                .sum();

        int matchedWeight = submitted.stream()
                .filter(submittedType ->
                        required.stream().anyMatch(
                                req -> req.getId() != null
                                        && req.getId().equals(submittedType.getId())
                        ))
                .mapToInt(DocumentType::getWeight)
                .sum();

        return (matchedWeight * 100.0) / totalWeight;
    }

   
    public String deriveRating(double score) {

        if (score >= 90.0) {
            return "EXCELLENT";
        }
        if (score >= 75.0) {
            return "GOOD";
        }
        if (score >= 50.0) {
            return "POOR";
        }
        return "NON_COMPLIANT";
    }
}
