package com.example.demo.controller;

import com.example.demo.model.ComplianceRule;
import com.example.demo.service.ComplianceRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compliance-rules")
public class ComplianceRuleController {

    private final ComplianceRuleService complianceRuleService;

    public ComplianceRuleController(ComplianceRuleService complianceRuleService) {
        this.complianceRuleService = complianceRuleService;
    }

    @PostMapping
    public ComplianceRule create(@RequestBody ComplianceRule rule) {
        return complianceRuleService.create(rule);
    }

    @GetMapping("/{id}")
    public ComplianceRule get(@PathVariable Long id) {
        return complianceRuleService.getById(id);
    }

    @GetMapping
    public List<ComplianceRule> getAll() {
        return complianceRuleService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        complianceRuleService.delete(id);
    }
}