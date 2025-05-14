package com.inventa.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminController {
    @GetMapping("/admin/login")
    public String redirectToLogin() {
        return "admin/login.html";
    }
    

    @GetMapping("/admin/reset-password")
    public String redirectToResetPassword() {
        return "admin/reset-password.html";
    }

    @GetMapping("/admin/dashboard")
    public String redirectToDashboard() {
        return "admin/dashboard.html";
    }

    @GetMapping("/admin/item-management")
    public String redirectToItemManagement() {
        return "admin/item-management.html";
    }

    @GetMapping("/admin/supplier-management")
    public String redirectToSupplierManagement() {
        return "admin/supplier-management.html";
    }

    @GetMapping("/admin/add-supplier")
    public String redirectToAddSupplier() {
        return "admin/add-supplier.html";
    }

    @GetMapping("/admin/transaction-management")
    public String redirectToTransactionManagement() {
        return "admin/transaction-management.html";
    }

    @GetMapping("/admin/report-management")
    public String redirectToReportManagement() {
        return "admin/report-management.html";
    }

    @GetMapping("/admin/change-password")
    public String redirectToChangePassword() {
        return "admin/change-password.html";
    }
}
