package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.ComplianceScore;
import com.example.demo.model.DocumentType;
import com.example.demo.model.Vendor;
import com.example.demo.model.VendorDocument;
import com.example.demo.repository.ComplianceScoreRepository;
import com.example.demo.repository.DocumentTypeRepository;
import com.example.demo.repository.VendorDocumentRepository;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.ComplianceScoreService;
import com.example.demo.util.ComplianceScoringEngine;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplianceScoreServiceImpl implements ComplianceScoreService {
    
    private final VendorRepository vendorRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final VendorDocumentRepository vendorDocumentRepository;
    private final ComplianceScoreRepository complianceScoreRepository;
    
    public ComplianceScoreServiceImpl(VendorRepository vendorRepository,
                                      DocumentTypeRepository documentTypeRepository,
                                      VendorDocumentRepository vendorDocumentRepository,
                                      ComplianceScoreRepository complianceScoreRepository) {
        this.vendorRepository = vendorRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.vendorDocumentRepository = vendorDocumentRepository;
        this.complianceScoreRepository = complianceScoreRepository;
    }
    
    @Override
    public ComplianceScore evaluateVendor(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        
        List<DocumentType> requiredTypes = documentTypeRepository.findByRequiredTrue();
        List<VendorDocument> vendorDocuments = vendorDocumentRepository.findByVendor(vendor);
        
        int totalRequiredDocuments = requiredTypes.size();
        int totalWeightRequired = requiredTypes.stream()
                .mapToInt(dt -> dt.getWeight() != null ? dt.getWeight() : 0)
                .sum();
        
        int uploadedValidDocuments = 0;
        int uploadedValidWeight = 0;
        
        for (DocumentType requiredType : requiredTypes) {
            boolean hasValidDocument = vendorDocuments.stream()
                    .anyMatch(vd -> vd.getDocumentType().getId().equals(requiredType.getId())
                            && vd.getIsValid() != null && vd.getIsValid());
            
            if (hasValidDocument) {
                uploadedValidDocuments++;
                uploadedValidWeight += (requiredType.getWeight() != null ? requiredType.getWeight() : 0);
            }
        }
        
        double scoreValue = ComplianceScoringEngine.calculateScore(
                totalRequiredDocuments,
                uploadedValidDocuments,
                totalWeightRequired,
                uploadedValidWeight
        );
        
        if (scoreValue < 0) {
            throw new ValidationException("Compliance score cannot be negative");
        }
        
        String rating = ComplianceScoringEngine.deriveRating(scoreValue);
        
        Optional<ComplianceScore> existingScore = complianceScoreRepository.findByVendorId(vendorId);
        
        ComplianceScore score;
        if (existingScore.isPresent()) {
            score = existingScore.get();
            score.setScoreValue(scoreValue);
            score.setRating(rating);
            score.setLastEvaluated(LocalDateTime.now());
        } else {
            score = new ComplianceScore(vendor, scoreValue, rating);
        }
        
        return complianceScoreRepository.save(score);
    }
    
    @Override
    public ComplianceScore getScore(Long vendorId) {
        return complianceScoreRepository.findByVendorId(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found"));
    }
    
    @Override
    public List<ComplianceScore> getAllScores() {
        return complianceScoreRepository.findAll();
    }
}