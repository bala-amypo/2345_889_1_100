package com.example.demo.repository;

import com.example.demo.model.Vendor;
import com.example.demo.model.VendorDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VendorDocumentRepository
        extends JpaRepository<VendorDocument, Long> {

    List<VendorDocument> findByVendor(Vendor vendor);

    @Query("SELECT v FROM VendorDocument v WHERE v.expiryDate < :date")
    List<VendorDocument> findExpiredDocuments(@Param("date") LocalDate date);
}
