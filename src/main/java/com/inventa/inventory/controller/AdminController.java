package com.inventa.inventory.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/login")
    public String redirectToLogin(HttpSession session) {
        Object user = session.getAttribute("user");

        if(user != null) {
            return "redirect:/admin/dashboard";
        }

        return "admin/login";
    }
    

    @GetMapping("/reset-password")
    public String redirectToResetPassword(HttpSession session) {
        Object user = session.getAttribute("user");

        if(user != null) {
            return "redirect:/admin/dashboard";
        }

        return "admin/reset-password";
    }

    @GetMapping("/dashboard")
    public String redirectToDashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/dashboard";
    }

    @GetMapping("/item-management")
    public String redirectToItemManagement(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/item-management";
    }

    @GetMapping("/transaction-management")
    public String redirectToTransactionManagement(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/transaction-management";
    }

    @GetMapping("/input-transaction")
    public String redirectToInputTransaction(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/input-transaction";
    }

    @GetMapping("/report-management")
    public String redirectToReportManagement(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/report-management";
    }

    @GetMapping("/change-password")
    public String redirectToChangePassword(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/change-password";
    }
}
