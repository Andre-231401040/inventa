package com.inventa.inventory.controller.admin;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventa.inventory.service.AuthService;

@Controller("authController")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/admin/login")
    public String redirectToLogin(HttpSession session) {
        Object user = session.getAttribute("user");

        if(user != null) {
            return "redirect:/admin/dashboard";
        }

        return "admin/login";
    }

    @GetMapping("/admin/reset-password")
    public String redirectToResetPassword(HttpSession session) {
        Object user = session.getAttribute("user");

        if(user != null) {
            return "redirect:/admin/dashboard";
        }

        return "admin/reset-password";
    }

    @GetMapping("/admin/change-password")
    public String redirectToChangePassword(HttpSession session) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/change-password";
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        boolean success = authService.login(email, password);

        if (success) {
            session.setAttribute("user", email);
            return "redirect:/admin/dashboard";  // redirect ke halaman home
        } else {
            redirectAttributes.addFlashAttribute("error", "Login gagal");
            return "redirect:/admin/login"; // kembali ke login page dengan pesan error
        }
    }

    @PostMapping("/auth/reset-password")
    public String resetPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        String result = authService.resetPassword(email);
 
        if(result == "User not found.") {
            redirectAttributes.addFlashAttribute("errorMessage", result);
        } else {
            redirectAttributes.addFlashAttribute("newPassword", result);
        }

        return "redirect:/admin/reset-password";
    }

    @PostMapping("/auth/change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = (String) session.getAttribute("user");
        String result = authService.changePassword(oldPassword, newPassword, confirmPassword, email);

        if(result == "Your old password is wrong.") {
            redirectAttributes.addFlashAttribute("errorMessage", result);
        } else if(result == "Confirm new password and new password must be the same.") {
            redirectAttributes.addFlashAttribute("errorMessage", result);
        } else if(result == "New password does not match the requirements.") {
            redirectAttributes.addFlashAttribute("errorMessage", result);
        } else {
            redirectAttributes.addFlashAttribute("successfulMessage", result);
        }

        return "redirect:/admin/change-password";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}