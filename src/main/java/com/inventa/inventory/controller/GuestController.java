package com.inventa.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.service.SupplierService;

@Controller
public class GuestController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/")
    public String redirectToGuestItems() {
        return "guest/items";
    }

    @GetMapping("/suppliers")
    public String redirectToGuestSuppliers(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 7;
        Page<Supplier> suppliersPerPage = supplierService.getSuppliersPerPage(page, pageSize);
        
        model.addAttribute("suppliers", suppliersPerPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", suppliersPerPage.getTotalPages());
        model.addAttribute("totalSuppliers", suppliersPerPage.getTotalElements());

        return "guest/suppliers";
    }

    @PostMapping("/suppliers/search")
    public String searchSuppliersByName(@RequestParam(defaultValue = "0") int page, @RequestParam String keyword, Model model) {
        int pageSize = 7;
        Page<Supplier> searchResult = supplierService.searchSuppliers(page, pageSize, keyword);

        model.addAttribute("suppliers", searchResult.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("totalSuppliers", searchResult.getTotalElements());

        return "guest/suppliers";
    }
}
