package com.example.demo.service;

import com.example.demo.model.ComplianceRule;
import java.util.List;

public interface ComplianceRuleService {
    ComplianceRule create(ComplianceRule rule);
    ComplianceRule getById(Long id);
    List<ComplianceRule> getAll();
    void delete(Long id);
}