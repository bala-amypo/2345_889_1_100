package com.example.demo.service;

import com.example.demo.model.ComplianceScore;

public interface ComplianceScoreService {

    ComplianceScore evaluateVendor(Long vendorId);

    ComplianceScore getScore(Long vendorId);
}
