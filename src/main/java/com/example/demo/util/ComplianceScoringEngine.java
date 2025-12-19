package com.example.demo.util;

public class ComplianceScoringEngine {

    public double calculateScore(int totalWeight, int obtainedWeight) {
        if (totalWeight == 0) return 0;
        return (obtainedWeight * 100.0) / totalWeight;
    }

    public String deriveRating(double score) {
        if (score >= 90) return "EXCELLENT";
        if (score >= 70) return "GOOD";
        if (score >= 40) return "POOR";
        return "NONCOMPLIANT";
    }
}