package com.example.demo.service;

import com.example.demo.model.ComplianceScore;

import java.util.List;

public interface ComplianceScoreService {

    ComplianceScore evaluateVendor(Long vendorId);

    ComplianceScore getScore(Long vendorId);

    List<ComplianceScore> getAllScores();
}
