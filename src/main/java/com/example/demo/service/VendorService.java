package com.example.demo.service;

import com.example.demo.model.Vendor;

public interface VendorService {

    Vendor createVendor(Vendor vendor);

    Vendor getVendor(Long id);
}
