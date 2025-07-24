package com.banking.Banking_System.repository;

import com.banking.Banking_System.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accNo);
    List<Account> findByStatus(String status);

    long countByStatus(String status);

    @Query("SELECT SUM(a.balance) FROM Account a")
    Double sumAllBalances();

    // Find account by associated user id (assuming a one-to-one mapping)
    Account findByUserId(Long userId);
}