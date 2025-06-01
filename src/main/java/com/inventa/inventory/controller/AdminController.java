package com.inventa.inventory.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventa.inventory.model.Dashboard;
import com.inventa.inventory.model.Item;
import com.inventa.inventory.model.PdfExporter;
import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.model.Transaction;
import com.inventa.inventory.service.DashboardService;
import com.inventa.inventory.service.ItemService;
import com.inventa.inventory.service.SupplierService;
import com.inventa.inventory.service.TransactionService;
import com.inventa.inventory.service.ReportService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ReportService reportService;

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
    public String redirectToItemManagement(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        // Page<Item> itemsPerPage = itemService.getItems(page, pageSize);
        List<Item> content = itemService.getItems();

        for(int i = 0; i < content.size(); i++) {
            if(content.get(i).getStatus().equalsIgnoreCase("In")) {
                Item itemI = content.get(i);
                String itemInName = itemI.getName();
                Long stock = itemI.getStock();

                for(int j = 0; j < content.size(); j++) {
                    if(i == j) continue;

                    Item itemJ = content.get(j);
                    String itemName = itemJ.getName();
                    Long qty = itemJ.getStock();

                    if(itemName.equalsIgnoreCase(itemInName)) {
                        switch(itemJ.getStatus().toLowerCase()) {
                            case "out":
                            case "lent":
                                stock -= qty;
                                break;
                            case "returned":
                                stock += qty;
                                break;
                        }
                    }
                }

                content.get(i).setStock(stock);
            }
        }

        

        // ambil hanya yang statusnya In
        List<Item> filtered = content.stream().filter(item -> item.getStatus().equalsIgnoreCase("In")).collect(Collectors.toList());

        int start = page * pageSize;
        int end = Math.min(start + pageSize, filtered.size());
        List<Item> pageContent = filtered.subList(start, end);
        Page<Item> filteredItems = new PageImpl<>(pageContent, PageRequest.of(page, pageSize), filtered.size());
        
        model.addAttribute("items", filteredItems.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", filteredItems.getTotalPages());

        return "admin/item-management";
    }

    @GetMapping("/sort-item")
    public String sortItem(@RequestParam(defaultValue = "0") int page, @RequestParam String sortBy, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        // Page<Item> itemsPerPage = itemService.getItems(page, pageSize);
        List<Item> content = itemService.getItems();

        for(int i = 0; i < content.size(); i++) {
            if(content.get(i).getStatus().equalsIgnoreCase("In")) {
                Item itemI = content.get(i);
                String itemInName = itemI.getName();
                Long stock = itemI.getStock();

                for(int j = 0; j < content.size(); j++) {
                    if(i == j) continue;

                    Item itemJ = content.get(j);
                    String itemName = itemJ.getName();
                    Long qty = itemJ.getStock();

                    if(itemName.equalsIgnoreCase(itemInName)) {
                        switch(itemJ.getStatus().toLowerCase()) {
                            case "out":
                            case "lent":
                                stock -= qty;
                                break;
                            case "returned":
                                stock += qty;
                                break;
                        }
                    }
                }

                content.get(i).setStock(stock);
            }
        }

        

        // ambil hanya yang statusnya In
        Stream<Item> stream = content.stream().filter(item -> item.getStatus().equalsIgnoreCase("In"));

        Comparator<Item> comparator;
        switch(sortBy.split("-")[0]) {
            case "name":
                comparator = Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER);
                break;
            case "category":
                comparator = Comparator.comparing(Item::getCategory, String.CASE_INSENSITIVE_ORDER);
                break;
            case "stock":
                comparator = Comparator.comparing(Item::getStock);
                break;
            default:
                comparator = Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER);
                break;
        }

        if(sortBy.split("-")[1].equals("desc")) {
            comparator = comparator.reversed();
        }

        List<Item> filtered = stream.sorted(comparator).collect(Collectors.toList());

        int start = page * pageSize;
        int end = Math.min(start + pageSize, filtered.size());
        List<Item> pageContent = filtered.subList(start, end);
        Page<Item> filteredItems = new PageImpl<>(pageContent, PageRequest.of(page, pageSize), filtered.size());
        
        model.addAttribute("items", filteredItems.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", filteredItems.getTotalPages());

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

    @GetMapping("/get-transaction-by-status")
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
    public String redirectToReportManagement(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }
        
        int pageSize = 5;
        Page<Transaction> reports = reportService.getReports(page, pageSize);
        
        model.addAttribute("reports", reports.getContent());
        model.addAttribute("from", null);
        model.addAttribute("to", null);
        model.addAttribute("status", "");
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", reports.getTotalPages());

        return "admin/report-management";
    }

    @GetMapping("/report-management/filter")
    public String redirectToFilteredReportManagement(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to, @RequestParam(required = false) String status, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }
        
        int pageSize = 5;
        List<Transaction> data = reportService.getFilteredReports(from, to, status);
        
        // buat menjadi page
        int start = page * pageSize;
        int end = Math.min(start + pageSize, data.size());
        List<Transaction> pageContent = data.subList(start, end);
        Page<Transaction> reports = new PageImpl<>(pageContent, PageRequest.of(page, pageSize), data.size());

        model.addAttribute("reports", reports.getContent());
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", reports.getTotalPages());

        return "admin/report-management";
    }

    @GetMapping("/report-management/download-pdf")
    public String downloadReport(HttpServletResponse response, @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to, @RequestParam(required = false) String status, HttpSession session) throws IOException {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }
        
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report.pdf");

        List<Transaction> filteredReports = reportService.getFilteredReports(from, to, status);

        try {
            PdfExporter.export(filteredReports, from, to, status, response.getOutputStream());
            response.getOutputStream().flush();
        } catch(Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to Download PDF");
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
