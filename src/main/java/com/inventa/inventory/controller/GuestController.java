package com.inventa.inventory.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventa.inventory.model.Item;
import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.service.SupplierService;
import com.inventa.inventory.service.ItemService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GuestController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/")
    public String redirectToGuestItems(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        int pageSize = 8;
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
        model.addAttribute("totalItems", filteredItems.getTotalElements());

        return "guest/items";
    }

    @GetMapping("/items/search")
    public String searchItemsByName(@RequestParam(defaultValue = "0") int page, @RequestParam String keyword, HttpSession session, Model model) {
        int pageSize = 8;
        List<Item> content = itemService.getItemsByName(keyword);

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
        model.addAttribute("totalItems", filteredItems.getTotalElements());

        return "guest/items";
    }

    @GetMapping("/suppliers")
    public String redirectToGuestSuppliers(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 7;
        Page<Supplier> suppliersPerPage = supplierService.getSuppliersPerPage(page, pageSize);
        
        model.addAttribute("suppliers", suppliersPerPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", suppliersPerPage.getTotalPages());
        model.addAttribute("totalSuppliers", suppliersPerPage.getTotalElements());

        return "guest/suppliers";
    }

    @GetMapping("/suppliers/search")
    public String searchSuppliersByName(@RequestParam(defaultValue = "0") int page, @RequestParam String keyword, Model model) {
        int pageSize = 7;
        Page<Supplier> searchResult = supplierService.searchSuppliers(page, pageSize, keyword);

        model.addAttribute("suppliers", searchResult.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("totalSuppliers", searchResult.getTotalElements());

        return "guest/suppliers";
    }
}
