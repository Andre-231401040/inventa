package com.inventa.inventory.controller.admin;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.inventa.inventory.model.User;
import com.inventa.inventory.repository.UserRepository;
import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.service.SupplierService;

@Controller("adminSupplierController")
@RequestMapping("/admin")
public class SupplierController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/supplier-management")
    public String redirectToSupplierManagement(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        Page<Supplier> suppliersPerPage = supplierService.getSuppliersPerPage(page, pageSize);
        
        model.addAttribute("suppliers", suppliersPerPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", suppliersPerPage.getTotalPages());

        return "admin/supplier-management";
    }

    @GetMapping("/add-supplier-form")
    public String redirectToAddSupplier(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/add-supplier";
    }

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

    @GetMapping("/edit-supplier")
    public String showEditSupplierForm(HttpSession session) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/edit-supplier";
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
