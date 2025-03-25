package com.inventa.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/change-password.html")
    public String redirectToChangePasswordPage() {
        return "admin/change-password";
    }
}
