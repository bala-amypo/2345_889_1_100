package com.example.demo.service;

import com.example.demo.model.ComplianceScore;
import java.util.List;

public interface ComplianceScoreService {
    ComplianceScore create(ComplianceScore score);
    ComplianceScore getById(Long id);
    List<ComplianceScore> getAll();
    ComplianceScore calculateScore(Long vendorId);
    void delete(Long id);
}