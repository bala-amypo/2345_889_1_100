package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ComplianceRule;
import com.example.demo.repository.ComplianceRuleRepository;
import com.example.demo.service.ComplianceRuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplianceRuleServiceImpl implements ComplianceRuleService {

    private final ComplianceRuleRepository complianceRuleRepository;

    public ComplianceRuleServiceImpl(
            ComplianceRuleRepository complianceRuleRepository) {
        this.complianceRuleRepository = complianceRuleRepository;
    }

    @Override
    public ComplianceRule create(ComplianceRule rule) {
        return complianceRuleRepository.save(rule);
    }

    @Override
    public ComplianceRule getById(Long id) {
        return complianceRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
    }

    @Override
    public List<ComplianceRule> getAll() {
        return complianceRuleRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        complianceRuleRepository.delete(getById(id));
    }
}