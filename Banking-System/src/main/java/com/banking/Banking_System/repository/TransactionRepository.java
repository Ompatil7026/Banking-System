package com.banking.Banking_System.repository;

import com.banking.Banking_System.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Correct method name to traverse nested property 'user.id'
    Page<Transaction> findByUser_IdOrderByTimestampDesc(Long userId, Pageable pageable);
}