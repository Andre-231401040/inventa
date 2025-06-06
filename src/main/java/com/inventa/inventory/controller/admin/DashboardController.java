package com.inventa.inventory.controller.admin;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.inventa.inventory.model.Dashboard;
import com.inventa.inventory.service.DashboardService;

@Controller("dashboardController")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin/dashboard")
    public String redirectToDashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        Dashboard dashboardData = dashboardService.getDashboardData();

        model.addAttribute("dashboardData", dashboardData);

        return "admin/dashboard";
    }
}
