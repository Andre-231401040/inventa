package com.inventa.inventory.controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventa.inventory.model.User;
import com.inventa.inventory.repository.UserRepository;
import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.service.SupplierService;
import com.inventa.inventory.model.Transaction;
import com.inventa.inventory.service.TransactionService;

@Controller("transactionController")
@RequestMapping("/admin")
public class TransactionController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private TransactionService transactionService;
    
    @GetMapping("/transaction-management")
    public String redirectToTransactionManagement(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        Page<Transaction> transactions = transactionService.getTransactionsPerPage(page, pageSize);
        
        setModelAttributes(transactions, page, model);

        return "admin/transaction-management";
    }

    @GetMapping("/get-transaction-by-status")
    public String getTransactionsByStatus(@RequestParam(defaultValue = "0") int page,  @RequestParam String status, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        Page<Transaction> transactions = transactionService.getTransactionsByStatus(page, pageSize, status);
        
        setModelAttributes(transactions, page, model);

        return "admin/transaction-management";
    }

    @GetMapping("/input-transaction/{status}")
    public String redirectToInputTransaction(HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Transaction> transactionsIn = transactionService.getTransactionsIn();
        List<Transaction> transactionsLent = transactionService.getTransactionsLent();

        setModelAttributes(suppliers, transactionsIn, transactionsLent, model);

        return "admin/input-transaction";
    }

    @PostMapping("/add-transaction-in")
    public String addTransactionIn(@RequestParam MultipartFile image_in, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam String location, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, @RequestParam Integer supplier_id, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);
        Supplier supplier = supplierService.findById(supplier_id);

        LocalDate date = LocalDate.now();
        
        // image handling
        String newFileName = imageHandling(image_in, redirectAttributes);

        // simpan transaksi ke database
        Transaction newTransaction = new Transaction(newFileName, name, category, pic, location, qty, fee, condition, description, "In", date, supplier, user.getId());
        Transaction savedTransaction = transactionService.addTransaction(newTransaction);

        if(savedTransaction == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
            return "redirect:/admin/input-transaction/items-in";
        } else {
            redirectAttributes.addFlashAttribute("success", "New Transaction In Added Successfully.");
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/add-transaction-out")
    public String addTransactionOut(@RequestParam MultipartFile image_out, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);

        LocalDate date = LocalDate.now();

        // cek stok
        Integer unavailableStock = transactionService.getQtyByNameAndStatus(name, "Out");
        unavailableStock += transactionService.getQtyByNameAndStatus(name, "Lent");
        Integer stock = transactionService.getQtyByNameAndStatus(name, "In");
        stock += transactionService.getQtyByNameAndStatus(name, "Returned");
        if(qty + unavailableStock > stock) {
            redirectAttributes.addFlashAttribute("error", "Qty cannot bigger than available stock.");
            return "redirect:/admin/input-transaction/items-out";
        }
        
        // image handling
        String newFileName = imageHandling(image_out, redirectAttributes);

        // simpan transaksi ke database
        Transaction newTransaction = new Transaction(newFileName, name, category, pic, "-", qty, fee, condition, description, "Out", date, null, user.getId());
        Transaction savedTransaction = transactionService.addTransaction(newTransaction);

        if(savedTransaction == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
            return "redirect:/admin/input-transaction/items-out";
        } else {
            redirectAttributes.addFlashAttribute("success", "New Transaction Out Added Successfully.");
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/add-transaction-lent")
    public String addTransactionLent(@RequestParam MultipartFile image_lent, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);

        LocalDate date = LocalDate.now();

        // cek stok
        Integer unavailableStock = transactionService.getQtyByNameAndStatus(name, "Out");
        unavailableStock += transactionService.getQtyByNameAndStatus(name, "Lent");
        Integer stock = transactionService.getQtyByNameAndStatus(name, "In");
        stock += transactionService.getQtyByNameAndStatus(name, "Returned");
        if(qty + unavailableStock > stock) {
            redirectAttributes.addFlashAttribute("error", "Qty cannot bigger than available stock.");
            return "redirect:/admin/input-transaction/items-lent";
        }
        
        // image handling
        String newFileName = imageHandling(image_lent, redirectAttributes);

        // simpan transaksi ke database
        Transaction newTransaction = new Transaction(newFileName, name, category, pic, "-", qty, fee, condition, description, "Lent", date, null, user.getId());
        Transaction savedTransaction = transactionService.addTransaction(newTransaction);

        if(savedTransaction == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
            return "redirect:/admin/input-transaction/items-lent";
        } else {
            redirectAttributes.addFlashAttribute("success", "New Transaction Lent Added Successfully.");
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/add-transaction-returned")
    public String addTransactionReturned(@RequestParam MultipartFile image_returned, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);

        LocalDate date = LocalDate.now();

        // cek stok yang dipinjam
        Integer lent = transactionService.getQtyByNameAndStatus(name, "Lent");
        if(qty > lent) {
            redirectAttributes.addFlashAttribute("error", "Qty cannot bigger than lent.");
            return "redirect:/admin/input-transaction/items-returned";
        }
        
        // image handling
        String newFileName = imageHandling(image_returned, redirectAttributes);

        // simpan transaksi ke database
        Transaction newTransaction = new Transaction(newFileName, name, category, pic, "-", qty, fee, condition, description, "Returned", date, null, user.getId());
        Transaction savedTransaction = transactionService.addTransaction(newTransaction);

        if(savedTransaction == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
            return "redirect:/admin/input-transaction/items-returned";
        } else {
            redirectAttributes.addFlashAttribute("success", "New Transaction Returned Added Successfully.");
        }

        return "redirect:/admin/transaction-management";
    }

    @GetMapping("/edit-transaction/{status}")
    public String redirectToEditTransaction(@ModelAttribute Transaction transaction, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Transaction> transactionsIn = transactionService.getTransactionsIn();
        List<Transaction> transactionsLent = transactionService.getTransactionsLent();

        model.addAttribute("transaction", transaction);
        setModelAttributes(suppliers, transactionsIn, transactionsLent, model);

        return "admin/edit-transaction";
    }

    @PostMapping("/authorize-edit-transaction")
    public String authorizeEditTransaction(@RequestParam Integer id, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String email = (String) session.getAttribute("user");
        User user = userRepository.findByEmail(email);
        Transaction transaction = transactionService.getTransactionById(id);
        String status = transaction.getStatus();

        if(user.getId() != transaction.getAdminId()) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action. You are not the owner of this transaction.");
            return "redirect:/admin/transaction-management";
        }
        
        if(status.equalsIgnoreCase("In")) {
            status = "items-in";
            redirectAttributes.addFlashAttribute("transaction_in", transaction);
        } else if(status.equalsIgnoreCase("Out")) {
            redirectAttributes.addFlashAttribute("transaction_out", transaction);
            status = "items-out";
        } else if(status.equalsIgnoreCase("Lent")) {
            redirectAttributes.addFlashAttribute("transaction_lent", transaction);
            status = "items-lent";
        } else {
            redirectAttributes.addFlashAttribute("transaction_returned", transaction);
            status = "items-returned";
        }

        return "redirect:/admin/edit-transaction/" + status;
    }

    @PostMapping("/update-transaction-in")
    public String updateTransactionIn(@RequestParam Integer id, @RequestParam MultipartFile image_in, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam String location, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, @RequestParam Integer supplier_id, RedirectAttributes redirectAttributes, HttpSession session) {
        // hapus file lama
        Transaction transaction = transactionService.getTransactionById(id);
        File file = new File("uploads/" + transaction.getImage());
        file.delete();

        Supplier supplier = supplierService.findById(supplier_id);
        LocalDate date = LocalDate.now();
        
        // image handling
        String newFileName = imageHandling(image_in, redirectAttributes);

        // simpan transaksi ke database
        Transaction updatedTransaction = transactionService.updateTransaction(id, newFileName, name, category, pic, location, qty, fee, condition, description, date, supplier);

        if(updatedTransaction == null) {
        redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Transaction updated successfully.");
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/update-transaction-out")
    public String updateTransactionOut(@RequestParam Integer id, @RequestParam MultipartFile image_out, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, RedirectAttributes redirectAttributes, HttpSession session) {
        // hapus file lama
        Transaction transaction = transactionService.getTransactionById(id);
        File file = new File("uploads/" + transaction.getImage());
        file.delete();

        LocalDate date = LocalDate.now();
        
        // cek stok
        Integer unavailableStock = transactionService.getQtyByNameAndStatus(name, "Out");
        unavailableStock += transactionService.getQtyByNameAndStatus(name, "Lent");
        unavailableStock -= transaction.getQty();
        Integer stock = transactionService.getQtyByNameAndStatus(name, "In");
        stock += transactionService.getQtyByNameAndStatus(name, "Returned");
        if(qty + unavailableStock > stock) {
            redirectAttributes.addFlashAttribute("error", "Qty cannot bigger than available stock.");
            return "redirect:/admin/transaction-management";
        }

        // image handling
        String newFileName = imageHandling(image_out, redirectAttributes);

        // simpan transaksi ke database
        Transaction updatedTransaction = transactionService.updateTransaction(id, newFileName, name, category, pic, "-", qty, fee, condition, description, date, null);

        if(updatedTransaction == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Transaction updated successfully.");
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/update-transaction-lent")
    public String updateTransactionLent(@RequestParam Integer id, @RequestParam MultipartFile image_lent, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, RedirectAttributes redirectAttributes, HttpSession session) {
        // hapus file lama
        Transaction transaction = transactionService.getTransactionById(id);
        File file = new File("uploads/" + transaction.getImage());
        file.delete();

        LocalDate date = LocalDate.now();
        
        // cek stok
        Integer unavailableStock = transactionService.getQtyByNameAndStatus(name, "Out");
        unavailableStock += transactionService.getQtyByNameAndStatus(name, "Lent");
        unavailableStock -= transaction.getQty();
        Integer stock = transactionService.getQtyByNameAndStatus(name, "In");
        stock += transactionService.getQtyByNameAndStatus(name, "Returned");
        if(qty + unavailableStock > stock) {
            redirectAttributes.addFlashAttribute("error", "Qty cannot bigger than available stock.");
            return "redirect:/admin/transaction-management";
        }

        // image handling
        String newFileName = imageHandling(image_lent, redirectAttributes);

        // simpan transaksi ke database
        Transaction updatedTransaction = transactionService.updateTransaction(id, newFileName, name, category, pic, "-", qty, fee, condition, description, date, null);

        if(updatedTransaction == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Transaction updated successfully.");
        }

        return "redirect:/admin/transaction-management";
    }

    @PostMapping("/update-transaction-returned")
    public String updateTransactionReturned(@RequestParam Integer id, @RequestParam MultipartFile image_returned, @RequestParam String name, @RequestParam String category, @RequestParam String pic, @RequestParam Integer qty, @RequestParam Float fee, @RequestParam String condition, @RequestParam String description, RedirectAttributes redirectAttributes, HttpSession session) {
        // hapus file lama
        Transaction transaction = transactionService.getTransactionById(id);
        File file = new File("uploads/" + transaction.getImage());
        file.delete();

        LocalDate date = LocalDate.now();
        
        // cek stok yang dipinjam
        Integer lent = transactionService.getQtyByNameAndStatus(name, "Lent");
        if(qty > lent) {
            redirectAttributes.addFlashAttribute("error", "Qty cannot bigger than lent.");
            return "redirect:/admin/transaction-management";
        }

        // image handling
        String newFileName = imageHandling(image_returned, redirectAttributes);

        // simpan transaksi ke database
        Transaction updatedTransaction = transactionService.updateTransaction(id, newFileName, name, category, pic, "-", qty, fee, condition, description, date, null);

        if(updatedTransaction == null) {
            redirectAttributes.addFlashAttribute("error", "Oops! Something went wront.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Transaction updated successfully.");
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

    public void setModelAttributes(Page<Transaction> transactions, int page, Model model) {
        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", transactions.getTotalPages());
    }

    public void setModelAttributes(List<Supplier> suppliers, List<Transaction> transactionsIn, List<Transaction> transactionsLent, Model model) {
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("transactionsIn", transactionsIn);
        model.addAttribute("transactionsLent", transactionsLent);
    }

    public String imageHandling(MultipartFile image, RedirectAttributes redirectAttributes) {
        try {
            String fileName = image.getOriginalFilename();
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
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;
        } catch(IOException e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
            return "redirect:/admin/transaction-management";
        }
    }
}
