package com.example.demo.controller;

import com.example.demo.model.ComplianceScore;
import com.example.demo.service.ComplianceScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance-scores")
@Tag(name = "Compliance Scores")
@SecurityRequirement(name = "bearer-jwt")
public class ComplianceScoreController {
    
    private final ComplianceScoreService complianceScoreService;
    
    public ComplianceScoreController(ComplianceScoreService complianceScoreService) {
        this.complianceScoreService = complianceScoreService;
    }
    
    @PostMapping("/evaluate")
    @Operation(summary = "Evaluate compliance score for a vendor")
    public ResponseEntity<ComplianceScore> evaluateVendor(@RequestParam Long vendorId) {
        ComplianceScore score = complianceScoreService.evaluateVendor(vendorId);
        return ResponseEntity.ok(score);
    }
    
    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get compliance score for a vendor")
    public ResponseEntity<ComplianceScore> getScore(@PathVariable Long vendorId) {
        ComplianceScore score = complianceScoreService.getScore(vendorId);
        return ResponseEntity.ok(score);
    }
    
    @GetMapping
    @Operation(summary = "Get all compliance scores")
    public ResponseEntity<List<ComplianceScore>> getAllScores() {
        List<ComplianceScore> scores = complianceScoreService.getAllScores();
        return ResponseEntity.ok(scores);
    }
}