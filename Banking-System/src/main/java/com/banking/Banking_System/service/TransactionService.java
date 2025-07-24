package com.banking.Banking_System.service;

import com.banking.Banking_System.entities.Account;
import com.banking.Banking_System.entities.Transaction;
import com.banking.Banking_System.entities.User;
import com.banking.Banking_System.repository.AccountRepository;
import com.banking.Banking_System.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired private TransactionRepository txnRepo;
    @Autowired private AccountRepository accountRepo;
    @Autowired
    private AuditLogService auditService;

    @Transactional
    public String transferFunds(String fromAcc, String toAcc, double amount, User initiator, String ip) {
        Account sender = accountRepo.findByAccountNumber(fromAcc)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Account receiver = accountRepo.findByAccountNumber(toAcc)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getBalance() < amount) throw new RuntimeException("Insufficient funds");

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        // Save accounts
        accountRepo.save(sender);
        accountRepo.save(receiver);

        // Log the transaction
        Transaction txn = new Transaction();
        txn.setTransactionId(UUID.randomUUID().toString().replace("-","").substring(0, 15));
        txn.setFromAccount(sender);
        txn.setToAccount(receiver);
        txn.setAmount(amount);
        txn.setTimestamp(LocalDateTime.now());
        txn.setStatus("SUCCESS");
        txnRepo.save(txn);

        // Audit log
        auditService.logAction(initiator, "Transferred " + amount
                + " from " + fromAcc + " to " + toAcc, ip);

        return txn.getTransactionId();
    }

    public Page<Transaction> getTransactionsByUserId(Long userId, Pageable pageable) {
        return txnRepo.findByUser_IdOrderByTimestampDesc(userId, pageable);
    }

}
