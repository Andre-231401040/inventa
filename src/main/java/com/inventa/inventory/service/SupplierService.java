package com.inventa.inventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.repository.SupplierRepository;

@Service
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers;
    }

    public Supplier getSupplierById(Integer id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        return supplier.isPresent() ? supplier.get() : null;
    }

    public Page<Supplier> getSuppliersPerPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return supplierRepository.findAll(pageable);
    }

    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Integer id, String name, String contact, String address) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        Supplier newSupplier = supplier.isPresent() ? supplier.get() : null;

        newSupplier.setName(name);
        newSupplier.setContact(contact);
        newSupplier.setAddress(address);
        
        Supplier updatedSupplier = supplierRepository.save(newSupplier);

        return updatedSupplier;
    }

    public boolean deleteSupplierById(Integer id) {
        if(supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Page<Supplier> searchSuppliers(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return supplierRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Supplier findById(Integer id) {
        Optional<Supplier> foundSupplier = supplierRepository.findById(id);
        Supplier supplier = foundSupplier.isPresent() ? foundSupplier.get() : null;
        
        return supplier;
    }
}
