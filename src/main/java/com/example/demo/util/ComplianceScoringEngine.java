package com.example.demo.util;

public class ComplianceScoringEngine {
    
    public static double calculateScore(int totalRequiredDocuments, int uploadedValidDocuments, int totalWeightRequired, int uploadedValidWeight) {
        if (totalRequiredDocuments == 0) {
            return 100.0;
        }
        
        double documentPercentage = (double) uploadedValidDocuments / totalRequiredDocuments;
        double weightPercentage = totalWeightRequired > 0 ? (double) uploadedValidWeight / totalWeightRequired : 0.0;
        
        // Average of both percentages
        double score = ((documentPercentage + weightPercentage) / 2.0) * 100.0;
        
        return Math.max(0.0, Math.min(100.0, score));
    }
    
    public static String deriveRating(double score) {
        if (score >= 90) {
            return "EXCELLENT";
        } else if (score >= 70) {
            return "GOOD";
        } else if (score >= 50) {
            return "POOR";
        } else {
            return "NONCOMPLIANT";
        }
    }
}