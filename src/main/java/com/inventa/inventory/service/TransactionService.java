package com.inventa.inventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

    public boolean deleteTransactionById(Integer id) {
        if(transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
