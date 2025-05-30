package com.inventa.inventory.controller;

import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.model.User;
import com.inventa.inventory.service.SupplierService;
import com.inventa.inventory.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add-supplier")
    public String addSupplier(@RequestParam String name, @RequestParam String contact, @RequestParam String address, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);
        
        Supplier newSupplier = new Supplier(name, contact, address, user.getId());

        Supplier savedSupplier = supplierService.addSupplier(newSupplier);

        if(savedSupplier == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
        } else {
            redirectAttributes.addFlashAttribute("success", "New Supplier Added Successfully.");
        }
        
        return "redirect:/admin/supplier-management";
    }
    
    @PostMapping("/authorize-edit-supplier")
    public String authorizeEditSupplier(@RequestParam Integer id, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);
        Supplier supplier = supplierService.getSupplierById(id);

        if(user.getId() != supplier.getAdminId()) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action. You are not the owner of this supplier.");
            return "redirect:/admin/supplier-management";
        }

        model.addAttribute("supplier", supplier);

        return "admin/edit-supplier";
    }

    @PostMapping("/update-supplier")
    public String updateSupplier(@RequestParam Integer id, @RequestParam String name, @RequestParam String contact, @RequestParam String address, RedirectAttributes redirectAttributes) {
        Supplier updatedSupplier = supplierService.updateSupplier(id, name, contact, address);

        if(updatedSupplier == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Supplier updated successfully.");
        }

        return "redirect:/admin/supplier-management";
    }

    @PostMapping("/delete-supplier")
    public String deleteSupplier(@RequestParam Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);
        Supplier supplier = supplierService.getSupplierById(id);

        if(user.getId() != supplier.getAdminId()) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action. You are not the owner of this supplier.");
            return "redirect:/admin/supplier-management";
        }

        boolean deleted = supplierService.deleteSupplierById(id);

        if(deleted) {
            redirectAttributes.addFlashAttribute("success", "Supplier Deleted Successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
        }

        return "redirect:/admin/supplier-management";
    }
}
