package com.example.demo.controller;

import com.example.demo.model.ComplianceScore;
import com.example.demo.service.ComplianceScoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compliance-scores")
public class ComplianceScoreController {

    private final ComplianceScoreService complianceScoreService;

    public ComplianceScoreController(ComplianceScoreService complianceScoreService) {
        this.complianceScoreService = complianceScoreService;
    }

    @PostMapping
    public ComplianceScore create(@RequestBody ComplianceScore score) {
        return complianceScoreService.create(score);
    }

    @GetMapping("/{id}")
    public ComplianceScore get(@PathVariable Long id) {
        return complianceScoreService.getById(id);
    }

    @GetMapping
    public List<ComplianceScore> getAll() {
        return complianceScoreService.getAll();
    }

    @PostMapping("/calculate/{vendorId}")
    public ComplianceScore calculate(@PathVariable Long vendorId) {
        return complianceScoreService.calculateScore(vendorId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        complianceScoreService.delete(id);
    }
}