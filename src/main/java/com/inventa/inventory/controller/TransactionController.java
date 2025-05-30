package com.inventa.inventory.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventa.inventory.model.Transaction;
import com.inventa.inventory.model.User;
import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.repository.UserRepository;
import com.inventa.inventory.service.SupplierService;
import com.inventa.inventory.service.TransactionService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/admin")
public class TransactionController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/add-transaction-in")
    public String addTransactionIn(@RequestParam MultipartFile image_in, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam String location, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, @RequestParam Integer supplier_id, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            String status = "In";
            String email = (String) session.getAttribute("user");
            User user = userRepository.findByEmail(email);
            Supplier supplier = supplierService.findById(supplier_id);

            LocalDate date = LocalDate.now();
            
            // image handling
            String fileName = image_in.getOriginalFilename();
            String extension = "";

            int dotIndex = fileName.lastIndexOf(".");
            if(dotIndex >= 0) {
                extension = fileName.substring(dotIndex);
            }

            // buat nama file unik
            String newFileName = UUID.randomUUID().toString() + extension;

            String uploadDir = "uploads/";
            Path filePath = Paths.get(uploadDir, newFileName);
            Files.createDirectories(filePath.getParent());

            // simpan file fisik
            Files.copy(image_in.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // simpan transaksi ke database
            Transaction newTransaction = new Transaction(newFileName, name, category, pic, location, qty, fee, condition, description, status, date, supplier, user.getId());
            Transaction savedTransaction = transactionService.addTransaction(newTransaction);

            if(savedTransaction == null) {
                redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
                return "redirect:/admin/input-transaction/items-in";
            } else {
                redirectAttributes.addFlashAttribute("success", "New Transaction In Added Successfully.");
            }
        } catch(IOException e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
            return "redirect:/admin/input-transaction/items-in";
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/add-transaction-out")
    public String addTransactionOut(@RequestParam MultipartFile image_out, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            String status = "Out";
            String email = (String) session.getAttribute("user");
            User user = userRepository.findByEmail(email);

            LocalDate date = LocalDate.now();

            redirectAttributes.addFlashAttribute("qty", qty);

            // cek stok
            Integer unavailableStock = transactionService.getQtyByNameAndStatus(name, "Out");
            unavailableStock += transactionService.getQtyByNameAndStatus(name, "Lent");
            Integer stock = transactionService.getQtyByNameAndStatus(name, "In");
            if(qty + unavailableStock > stock) {
                redirectAttributes.addFlashAttribute("error", "Qty cannot bigger than available stock.");
                return "redirect:/admin/input-transaction/items-out";
            }
            
            // image handling
            String fileName = image_out.getOriginalFilename();
            String extension = "";

            int dotIndex = fileName.lastIndexOf(".");
            if(dotIndex >= 0) {
                extension = fileName.substring(dotIndex);
            }

            // buat nama file unik
            String newFileName = UUID.randomUUID().toString() + extension;

            String uploadDir = "uploads/";
            Path filePath = Paths.get(uploadDir, newFileName);
            Files.createDirectories(filePath.getParent());

            // simpan file fisik
            Files.copy(image_out.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // simpan transaksi ke database
            Transaction newTransaction = new Transaction(newFileName, name, category, pic, "-", qty, fee, condition, description, status, date, null, user.getId());
            Transaction savedTransaction = transactionService.addTransaction(newTransaction);

            if(savedTransaction == null) {
                redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
                return "redirect:/admin/input-transaction/items-out";
            } else {
                redirectAttributes.addFlashAttribute("success", "New Transaction Out Added Successfully.");
            }
        } catch(IOException e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
            return "redirect:/admin/input-transaction/items-out";
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/add-transaction-lent")
    public String addTransactionLent(@RequestParam MultipartFile image_lent, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            String status = "Lent";
            String email = (String) session.getAttribute("user");
            User user = userRepository.findByEmail(email);

            LocalDate date = LocalDate.now();

            redirectAttributes.addFlashAttribute("qty", qty);

            // cek stok
            Integer unavailableStock = transactionService.getQtyByNameAndStatus(name, "Out");
            unavailableStock += transactionService.getQtyByNameAndStatus(name, "Lent");
            Integer stock = transactionService.getQtyByNameAndStatus(name, "In");
            if(qty + unavailableStock > stock) {
                redirectAttributes.addFlashAttribute("error", "Qty cannot bigger than available stock.");
                return "redirect:/admin/input-transaction/items-lent";
            }
            
            // image handling
            String fileName = image_lent.getOriginalFilename();
            String extension = "";

            int dotIndex = fileName.lastIndexOf(".");
            if(dotIndex >= 0) {
                extension = fileName.substring(dotIndex);
            }

            // buat nama file unik
            String newFileName = UUID.randomUUID().toString() + extension;

            String uploadDir = "uploads/";
            Path filePath = Paths.get(uploadDir, newFileName);
            Files.createDirectories(filePath.getParent());

            // simpan file fisik
            Files.copy(image_lent.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // simpan transaksi ke database
            Transaction newTransaction = new Transaction(newFileName, name, category, pic, "-", qty, fee, condition, description, status, date, null, user.getId());
            Transaction savedTransaction = transactionService.addTransaction(newTransaction);

            if(savedTransaction == null) {
                redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
                return "redirect:/admin/input-transaction/items-lent";
            } else {
                redirectAttributes.addFlashAttribute("success", "New Transaction Lent Added Successfully.");
            }
        } catch(IOException e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
            return "redirect:/admin/input-transaction/items-lent";
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/delete-transaction")
    public String deleteTransaction(@RequestParam Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);
        Transaction transaction = transactionService.getTransactionById(id);

        if(user.getId() != transaction.getAdminId()) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action. You are not the owner of this transaction.");
            return "redirect:/admin/transaction-management";
        }

        String uploadDir = "uploads/";
        File file = new File(uploadDir + transaction.getImage());
        boolean deleted = transactionService.deleteTransactionById(id);

        if(deleted) {
            file.delete();
            redirectAttributes.addFlashAttribute("success", "Transaction Deleted Successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
        }

        return "redirect:/admin/transaction-management";
    }
}
