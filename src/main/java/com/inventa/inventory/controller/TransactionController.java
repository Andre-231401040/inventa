package com.inventa.inventory.controller;

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
import com.inventa.inventory.repository.UserRepository;
import com.inventa.inventory.service.TransactionService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/admin")
public class TransactionController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/add-transaction-in")
    public String addTransactionIn(@RequestParam MultipartFile image_in, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam String location, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, @RequestParam Integer supplier_id, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            String status = "In";
            String email = (String) session.getAttribute("user");
            User user = userRepository.findByEmail(email);

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
            Transaction newTransaction = new Transaction(newFileName, name, category, pic, location, qty, fee, condition, description, status, date, supplier_id, user.getId());
            Transaction savedTransaction = transactionService.addTransaction(newTransaction);

            if(savedTransaction == null) {
                redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
            } else {
                redirectAttributes.addFlashAttribute("success", "New Transaction In Added Successfully.");
            }
        } catch(IOException e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }

        return "redirect:/admin/transaction-management";
    }
    
}
