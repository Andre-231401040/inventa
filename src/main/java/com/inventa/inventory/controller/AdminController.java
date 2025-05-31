package com.inventa.inventory.controller;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventa.inventory.model.Dashboard;
import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.model.Transaction;
import com.inventa.inventory.service.DashboardService;
import com.inventa.inventory.service.SupplierService;
import com.inventa.inventory.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private TransactionService transactionService;

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

        Dashboard dashboardData = dashboardService.getDashboardData();

        model.addAttribute("dashboardData", dashboardData);

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

    @GetMapping("/add-supplier")
    public String redirectToAddSupplier(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/add-supplier";
    }

    @GetMapping("/edit-supplier")
    public String showEditSupplierForm(HttpSession session) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        return "admin/edit-supplier";
    }

    @GetMapping("/transaction-management")
    public String redirectToTransactionManagement(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        Page<Transaction> transactionsPerPage = transactionService.getTransactionsPerPage(page, pageSize);
        
        model.addAttribute("transactions", transactionsPerPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", transactionsPerPage.getTotalPages());

        return "admin/transaction-management";
    }

    @PostMapping("/get-transaction-by-status")
    public String getTransactionsByStatus(@RequestParam(defaultValue = "0") int page,  @RequestParam String status, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        Page<Transaction> transactionsPerPage = transactionService.getTransactionsByStatus(page, pageSize, status);
        
        model.addAttribute("transactions", transactionsPerPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", transactionsPerPage.getTotalPages());

        return "admin/transaction-management";
    }
    

    @GetMapping("/tes")
    public String redirectToTes(@ModelAttribute Transaction transaction, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Transaction> transactionsIn = transactionService.getTransactionsIn();
        List<Transaction> transactionsLent = transactionService.getTransactionsLent();

        if(user == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("transaction", transaction);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("transactionsIn", transactionsIn);
        model.addAttribute("transactionsLent", transactionsLent);

        return "admin/tes";
    }

    @GetMapping("/input-transaction/{status}")
    public String redirectToInputTransaction(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Transaction> transactionsIn = transactionService.getTransactionsIn();
        List<Transaction> transactionsLent = transactionService.getTransactionsLent();

        if(user == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("suppliers", suppliers);
        model.addAttribute("transactionsIn", transactionsIn);
        model.addAttribute("transactionsLent", transactionsLent);

        return "admin/input-transaction";
    }

    @GetMapping("/edit-transaction/{status}")
    public String showEditTransactionForm(@ModelAttribute Transaction transaction, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Transaction> transactionsIn = transactionService.getTransactionsIn();
        List<Transaction> transactionsLent = transactionService.getTransactionsLent();

        if(user == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("transaction", transaction);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("transactionsIn", transactionsIn);
        model.addAttribute("transactionsLent", transactionsLent);

        return "admin/edit-transaction";
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
