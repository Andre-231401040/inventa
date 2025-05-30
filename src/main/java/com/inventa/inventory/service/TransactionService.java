package com.inventa.inventory.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.inventa.inventory.model.Supplier;
import com.inventa.inventory.model.Transaction;
import com.inventa.inventory.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Integer getQtyByNameAndStatus(String name, String status) {
        List<Transaction> transaction = transactionRepository.findByNameAndStatus(name, status);

        return transaction.stream().mapToInt(Transaction::getQty).sum();
    }

    public Page<Transaction> getTransactionsPerPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return transactionRepository.findAll(pageable);
    }

    public Transaction getTransactionById(Integer id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.isPresent() ? transaction.get() : null;
    }

    public Page<Transaction> getTransactionsByStatus(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return !status.equalsIgnoreCase("All") ? transactionRepository.findByStatus(status, pageable) : transactionRepository.findAll(pageable);
    }

    public List<Transaction> getTransactionsIn() {
        List<Transaction> transaction = transactionRepository.findByStatus("In");
        return transaction;
    }

    public List<Transaction> getTransactionsLent() {
        List<Transaction> transaction = transactionRepository.findByStatus("Lent");
        return transaction;
    }

    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Integer id, String image, String name, String category, String pic, String location, Integer qty, Float fee, String condition, String description, LocalDate date, Supplier supplier) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        Transaction newTransaction = transaction.isPresent() ? transaction.get() : null;

        newTransaction.setImage(image);
        newTransaction.setName(name);
        newTransaction.setCategory(category);
        newTransaction.setPic(pic);
        newTransaction.setLocation(location);
        newTransaction.setQty(qty);
        newTransaction.setFee(fee);
        newTransaction.setCondition(condition);
        newTransaction.setDescription(description);
        newTransaction.setDate(date);
        newTransaction.setSupplier(supplier);
        
        Transaction updatedTransaction = transactionRepository.save(newTransaction);

        return updatedTransaction;
    }

    public boolean deleteTransactionById(Integer id) {
        if(transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
