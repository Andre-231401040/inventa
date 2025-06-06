package com.inventa.inventory.controller.guest;

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
import com.inventa.inventory.service.ItemService;

@Controller("guestItemController")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public String redirectToGuestItems(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 8;
        List<Item> items = itemService.getItems();

        Page<Item> filteredItems = getFilteredItems(items, page, pageSize);

        setModelAttributes(filteredItems, page, model);

        return "guest/items";
    }

    @GetMapping("/items/search")
    public String searchItemsByName(@RequestParam(defaultValue = "0") int page, @RequestParam String keyword, Model model) {
        int pageSize = 8;
        List<Item> items = itemService.getItemsByName(keyword);

        Page<Item> filteredItems = getFilteredItems(items, page, pageSize);

        setModelAttributes(filteredItems, page, model);

        return "guest/items";
    }

    public void setModelAttributes(Page<Item> filteredItems, int page, Model model) {
        model.addAttribute("items", filteredItems.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", filteredItems.getTotalPages());
        model.addAttribute("totalItems", filteredItems.getTotalElements());
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
}
