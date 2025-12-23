package com.example.demo.controller;

import com.example.demo.model.ComplianceRule;
import com.example.demo.service.ComplianceRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance-rules")
@Tag(name = "Compliance Rules")
@SecurityRequirement(name = "bearer-jwt")
public class ComplianceRuleController {
    
    private final ComplianceRuleService complianceRuleService;
    
    public ComplianceRuleController(ComplianceRuleService complianceRuleService) {
        this.complianceRuleService = complianceRuleService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new compliance rule")
    public ResponseEntity<ComplianceRule> createRule(@RequestBody ComplianceRule rule) {
        ComplianceRule createdRule = complianceRuleService.createRule(rule);
        return ResponseEntity.ok(createdRule);
    }
    
    @GetMapping
    @Operation(summary = "Get all compliance rules")
    public ResponseEntity<List<ComplianceRule>> getAllRules() {
        List<ComplianceRule> rules = complianceRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get compliance rule by ID")
    public ResponseEntity<ComplianceRule> getRule(@PathVariable Long id) {
        ComplianceRule rule = complianceRuleService.getRule(id);
        return ResponseEntity.ok(rule);
    }
}