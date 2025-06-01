package com.inventa.inventory.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inventa.inventory.model.Item;
import com.inventa.inventory.repository.TransactionRepository;

@Service
public class ItemService {
    
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Item> getItems() {
        return transactionRepository.findByNameAndStatus();
    }

    public List<Item> getItemsByName(String name) {
        return transactionRepository.findByNameAndStatus(name);
    }
}
