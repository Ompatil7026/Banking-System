package com.banking.Banking_System.service;

import com.banking.Banking_System.entities.Account;
import com.banking.Banking_System.entities.User;
import com.banking.Banking_System.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccountForCustomer(User user) {
        Account acc = new Account();
        acc.setAccountNumber("ACC" + System.currentTimeMillis());
        acc.setHolderName(user.getUsername());
        acc.setUser(user);
        acc.setBalance(0.0);
        acc.setStatus("ACTIVE");
        return accountRepo.save(acc);
    }
    // More: get account, update, delete, etc.

    public List<Account> getAllAccounts(){
        return accountRepo.findAll();
    }

    public List<Account> getPendingAccounts(){
        return accountRepo.findByStatus("PENDING");
    }

    // Approve account by id:
    @Transactional
    public void approveAccount(Long accountId, User manager, String clientIp) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setStatus("ACTIVE");
        accountRepo.save(account);

        auditLogService.logAction(manager, "Approved account #" + account.getAccountNumber(), clientIp);
    }


    // Reject account by id:
    public void rejectAccount(Long accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("REJECTED");
        accountRepo.save(account);
    }

    //for paging data of accounts
    public Page<Account> getAccountsPage(Pageable pageable) {
        return accountRepo.findAll(pageable);
    }


    // Returns the total number of accounts with a given status
    public long countByStatus(String status) {
        return accountRepo.countByStatus(status);
    }

    // Returns the sum of all balances in all accounts
    public double getTotalBalance() {
        Double sum = accountRepo.sumAllBalances();
        return sum != null ? sum : 0;
    }

    //Enable caching to check current balance
    @Cacheable(value = "accountBalances", key = "#userId")
    public Double getCurrentBalance(Long userId) {
        // Fetch balance from DB or repository
        Account account = accountRepo.findByUserId(userId);
        return account != null ? account.getBalance() : 0.0;
    }

    //method for transaction history
    @Cacheable(value = "accountBalances", key = "#userId")
    public Double getCachedBalance(Long userId) {
        Account account = accountRepository.findByUserId(userId);
        return account != null ? account.getBalance() : 0.0;
    }

}
