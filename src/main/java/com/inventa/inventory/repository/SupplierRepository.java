package com.inventa.inventory.repository;

import com.inventa.inventory.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier findByName(String name);
    Page<Supplier> findAll(Pageable pageable);
}
