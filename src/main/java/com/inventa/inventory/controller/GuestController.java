package com.inventa.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuestController {
    @GetMapping("/")
    public String redirectToGuestItems() {
        return "guest/items";
    }

    @GetMapping("/suppliers")
    public String redirectToGuestSuppliers() {
        return "guest/suppliers";
    }
}
