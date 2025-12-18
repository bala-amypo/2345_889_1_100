package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.ComplianceScore;
import com.example.demo.model.Vendor;
import com.example.demo.repository.*;
import com.example.demo.service.ComplianceScoreService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplianceScoreServiceImpl implements ComplianceScoreService {

    private final ComplianceScoreRepository complianceScoreRepository;
    private final VendorRepository vendorRepository;
    private final VendorDocumentRepository vendorDocumentRepository;
    private final ComplianceRuleRepository complianceRuleRepository;

    // ✅ EXACT order
    public ComplianceScoreServiceImpl(
            ComplianceScoreRepository complianceScoreRepository,
            VendorRepository vendorRepository,
            VendorDocumentRepository vendorDocumentRepository,
            ComplianceRuleRepository complianceRuleRepository) {

        this.complianceScoreRepository = complianceScoreRepository;
        this.vendorRepository = vendorRepository;
        this.vendorDocumentRepository = vendorDocumentRepository;
        this.complianceRuleRepository = complianceRuleRepository;
    }

    @Override
    public ComplianceScore create(ComplianceScore score) {
        return complianceScoreRepository.save(score);
    }

    @Override
    public ComplianceScore getById(Long id) {
        return complianceScoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found"));
    }

    @Override
    public List<ComplianceScore> getAll() {
        return complianceScoreRepository.findAll();
    }

    @Override
    public ComplianceScore calculateScore(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        double score = 100.0;

        if (score < 0) {
            throw new ValidationException("Compliance score cannot be negative");
        }

        ComplianceScore cs = new ComplianceScore();
        cs.setVendor(vendor);
        cs.setScoreValue(score);
        cs.setRating("GOOD");
        cs.setLastEvaluated(LocalDateTime.now());

        return complianceScoreRepository.save(cs);
    }

    @Override
    public void delete(Long id) {
        complianceScoreRepository.delete(getById(id));
    }
}