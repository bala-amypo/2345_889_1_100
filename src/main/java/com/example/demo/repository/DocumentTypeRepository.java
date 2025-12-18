package com.example.demo.repository;

import com.example.demo.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    boolean existsByTypeName(String typeName);
}