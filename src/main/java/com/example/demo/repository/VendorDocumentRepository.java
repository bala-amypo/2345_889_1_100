package com.example.demo.repository;

import com.example.demo.model.Vendor;
import com.example.demo.model.VendorDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VendorDocumentRepository extends JpaRepository<VendorDocument, Long> {

    List<VendorDocument> findByVendorId(Long vendorId);

    List<VendorDocument> findByVendor(Vendor vendor);

    @Query("SELECT vd FROM VendorDocument vd WHERE vd.expiryDate < :cutoffDate")
    List<VendorDocument> findExpiredDocuments(LocalDate cutoffDate);
}