package com.inventa.inventory.repository;

import com.inventa.inventory.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByStatus(String status);
    List<Transaction> findByNameAndStatus(String name, String status);
}
