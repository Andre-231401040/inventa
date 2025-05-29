package com.inventa.inventory.repository;

import com.inventa.inventory.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    // @Query(value = """
    //             SELECT t.id, t.image, t.name, t.category, t.pic, t.location, t.qty, t.fee, t.condition, t.description, t.status, t.date,
    //                    s.name AS supplier_name 
    //             FROM transactions t
    //             INNER JOIN suppliers s ON t.supplier_id = s.id
    //         """)
    // Page<Transaction> findTransactionsWithAdmin(Pageable pageable);
}
