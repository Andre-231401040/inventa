package com.inventa.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventa.inventory.model.Item;
import com.inventa.inventory.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.status = :status GROUP BY t.name, t.category")
    List<Transaction> findByStatus(@Param("status") String status);

    Page<Transaction> findByStatus(String status, Pageable pageable);

    List<Transaction> findByNameAndStatus(String name, String status);

    @Query("SELECT new com.inventa.inventory.model.Item(t.image, t.name, t.category, SUM(t.qty), t.status) FROM Transaction t GROUP BY t.name, t.status")
    List<Item> findByNameAndStatus();

    @Query("SELECT new com.inventa.inventory.model.Item(t.image, t.name, t.category, SUM(t.qty), t.status) FROM Transaction t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) GROUP BY t.name, t.status")
    List<Item> findByNameAndStatus(String name);
}
