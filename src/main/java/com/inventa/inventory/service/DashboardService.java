package com.inventa.inventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventa.inventory.model.Dashboard;
import com.inventa.inventory.model.Transaction;
import com.inventa.inventory.repository.SupplierRepository;
import com.inventa.inventory.repository.TransactionRepository;

@Service
public class DashboardService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public Dashboard getDashboardData() {
        List<Transaction> transactions = transactionRepository.findAll();
        Integer totalItems = 0;
        Integer itemsIn = 0;
        Integer itemsOut = 0;
        Integer itemsLent = 0;
        Integer totalSuppliers = supplierRepository.findAll().size();
        Integer totalTransactions = transactions.size();

        for(Transaction transaction : transactions) {
            if(transaction.getStatus().equalsIgnoreCase("In")) {
                itemsIn += transaction.getQty();
                totalItems += transaction.getQty();
            } else if(transaction.getStatus().equalsIgnoreCase("Out")) {
                itemsOut += transaction.getQty();
                totalItems -= transaction.getQty();
            } else if(transaction.getStatus().equalsIgnoreCase("Lent")) {
                itemsLent += transaction.getQty();
                totalItems -= transaction.getQty();
            } else {
                itemsLent -= transaction.getQty();
                totalItems += transaction.getQty();
            }
        }

        Dashboard dashboardData = new Dashboard(totalItems, itemsIn, itemsOut, itemsLent, totalSuppliers, totalTransactions);

        return dashboardData;
    }
}
