package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Vendor;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.VendorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Vendor create(Vendor vendor) {
        if (vendorRepository.existsByVendorName(vendor.getVendorName())) {
            throw new ValidationException("Vendor name already exists");
        }
        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor getById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
    }

    @Override
    public List<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor update(Long id, Vendor vendor) {
        Vendor existing = getById(id);
        existing.setVendorName(vendor.getVendorName());
        existing.setEmail(vendor.getEmail());
        existing.setPhone(vendor.getPhone());
        existing.setIndustry(vendor.getIndustry());
        return vendorRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        vendorRepository.delete(getById(id));
    }
}