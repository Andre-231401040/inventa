package com.inventa.inventory.controller.admin;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventa.inventory.model.Item;
import com.inventa.inventory.service.ItemService;

@Controller("adminItemController")
@RequestMapping("/admin")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/item-management")
    public String redirectToItemManagement(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        List<Item> items = itemService.getItems();

        Page<Item> filteredItems = getFilteredItems(items, page, pageSize);
        
        setModelAttributes(filteredItems, page, model);

        return "admin/item-management";
    }

    @GetMapping("/sort-item")
    public String sortItem(@RequestParam(defaultValue = "0") int page, @RequestParam String sortBy, HttpSession session, Model model) {
        Object user = session.getAttribute("user");

        if(user == null) {
            return "redirect:/admin/login";
        }

        int pageSize = 5;
        List<Item> items = itemService.getItems();

        Page<Item> filteredItems = getFilteredItems(items, page, pageSize, sortBy);
        
        setModelAttributes(filteredItems, page, model);

        return "admin/item-management";
    }

    public void setModelAttributes(Page<Item> filteredItems, int page, Model model) {
        model.addAttribute("items", filteredItems.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", filteredItems.getTotalPages());
    }

    public Page<Item> getFilteredItems(List<Item> items, int page, int pageSize) {
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getStatus().equalsIgnoreCase("In")) {
                Item itemI = items.get(i);
                String itemInName = itemI.getName();
                Long stock = itemI.getStock();

                for(int j = 0; j < items.size(); j++) {
                    if(i == j) continue;

                    Item itemJ = items.get(j);
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

                items.get(i).setStock(stock);
            }
        }

        

        // ambil hanya yang statusnya In
        List<Item> filtered = items.stream().filter(item -> item.getStatus().equalsIgnoreCase("In")).collect(Collectors.toList());

        int start = page * pageSize;
        int end = Math.min(start + pageSize, filtered.size());
        List<Item> pageContent = filtered.subList(start, end);
        Page<Item> filteredItems = new PageImpl<>(pageContent, PageRequest.of(page, pageSize), filtered.size());
    
        return filteredItems;
    }

    // overloading
    public Page<Item> getFilteredItems(List<Item> items, int page, int pageSize, String sortBy) {
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getStatus().equalsIgnoreCase("In")) {
                Item itemI = items.get(i);
                String itemInName = itemI.getName();
                Long stock = itemI.getStock();

                for(int j = 0; j < items.size(); j++) {
                    if(i == j) continue;

                    Item itemJ = items.get(j);
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

                items.get(i).setStock(stock);
            }
        }

        

        // ambil hanya yang statusnya In
        Stream<Item> stream = items.stream().filter(item -> item.getStatus().equalsIgnoreCase("In"));

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

        return filteredItems;
    }
}
