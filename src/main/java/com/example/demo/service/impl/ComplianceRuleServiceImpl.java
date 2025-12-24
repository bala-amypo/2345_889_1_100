package com.example.demo.service.impl;

import com.example.demo.model.ComplianceRule;
import com.example.demo.repository.ComplianceRuleRepository;
import com.example.demo.service.ComplianceRuleService;

public class ComplianceRuleServiceImpl implements ComplianceRuleService {

    private final ComplianceRuleRepository complianceRuleRepository;

    public ComplianceRuleServiceImpl(ComplianceRuleRepository complianceRuleRepository) {
        this.complianceRuleRepository = complianceRuleRepository;
    }

    @Override
    public ComplianceRule createRule(ComplianceRule rule) {
        return complianceRuleRepository.save(rule);
    }
}
