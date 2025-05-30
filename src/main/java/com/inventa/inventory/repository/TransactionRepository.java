package com.inventa.inventory.repository;

import com.inventa.inventory.model.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.status = :status GROUP BY t.name, t.category")
    List<Transaction> findByStatus(@Param("status") String status);
    Page<Transaction> findByStatus(String status, Pageable pageable);
    List<Transaction> findByNameAndStatus(String name, String status);
}
