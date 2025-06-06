package com.inventa.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inventa.inventory.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Page<Supplier> findAll(Pageable pageable);

    Supplier findByName(String name);

    Page<Supplier> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
