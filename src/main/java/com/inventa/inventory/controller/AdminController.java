package com.inventa.inventory.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminController {
    @GetMapping("/admin/login")
    public String redirectToLogin() {
        return "admin/login";
    }
    

    @GetMapping("/admin/reset-password")
    public String redirectToResetPassword() {
        return "admin/reset-password";
    }

    @GetMapping("/admin/dashboard")
    public String redirectToDashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("user", user);
        return "admin/dashboard";
    }

    @GetMapping("/admin/item-management")
    public String redirectToItemManagement() {
        return "admin/item-management";
    }

    @GetMapping("/admin/supplier-management")
    public String redirectToSupplierManagement() {
        return "admin/supplier-management";
    }

    @GetMapping("/admin/add-supplier")
    public String redirectToAddSupplier() {
        return "admin/add-supplier";
    }

    @GetMapping("/admin/transaction-management")
    public String redirectToTransactionManagement() {
        return "admin/transaction-management";
    }

    @GetMapping("/admin/input-transaction")
    public String redirectToInputTransaction() {
        return "admin/input-transaction";
    }

    @GetMapping("/admin/report-management")
    public String redirectToReportManagement() {
        return "admin/report-management";
    }

    @GetMapping("/admin/change-password")
    public String redirectToChangePassword() {
        return "admin/change-password";
    }
}
