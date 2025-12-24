package com.example.demo.service;

import com.example.demo.model.Vendor;
import java.util.List;

public interface VendorService {

    Vendor createVendor(Vendor vendor);

    Vendor getVendor(Long id);

    List<Vendor> getAllVendors();
}
