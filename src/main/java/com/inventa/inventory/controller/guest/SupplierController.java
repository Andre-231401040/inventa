package com.inventa.inventory.controller.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.service.SupplierService;

@Controller("guestSupplierController")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/suppliers")
    public String redirectToGuestSuppliers(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 7;
        Page<Supplier> suppliers = supplierService.getSuppliersPerPage(page, pageSize);
        
        setModelAttributes(suppliers, page, model);

        return "guest/suppliers";
    }

    @GetMapping("/suppliers/search")
    public String searchSuppliersByName(@RequestParam(defaultValue = "0") int page, @RequestParam String keyword, Model model) {
        int pageSize = 7;
        Page<Supplier> suppliers = supplierService.searchSuppliers(page, pageSize, keyword);

        setModelAttributes(suppliers, page, model);

        return "guest/suppliers";
    }

    public void setModelAttributes(Page<Supplier> suppliers, int page, Model model) {
        model.addAttribute("suppliers", suppliers.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", suppliers.getTotalPages());
        model.addAttribute("totalSuppliers", suppliers.getTotalElements());
    }
}
