package com.inventa.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/")
    public String redirectToGuestItems() {
        return "guest/items.html";
    }

    @GetMapping("/suppliers")
    public String redirectToGuestSuppliers() {
        return "guest/suppliers.html";
    }

    @GetMapping("/admin/reset-password")
    public String redirectToResetPassword() {
        return "admin/reset-password.html";
    }

    @GetMapping("/admin/change-password")
    public String redirectToChangePassword() {
        return "admin/change-password";
    }
}
