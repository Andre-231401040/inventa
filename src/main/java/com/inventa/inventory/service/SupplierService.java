package com.inventa.inventory.service;

import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.repository.SupplierRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;

    public Page<Supplier> getSuppliersPerPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return supplierRepository.findAll(pageable);
    }

    public Supplier addSupplier(Supplier supplier, HttpSession session) {
        return supplierRepository.save(supplier);
    }

    public Page<Supplier> searchSuppliers(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return supplierRepository.findByNameContainingIgnoreCase(name, pageable);
    } 
}
