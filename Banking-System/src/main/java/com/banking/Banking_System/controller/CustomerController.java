package com.banking.Banking_System.controller;

import com.banking.Banking_System.entities.Account;
import com.banking.Banking_System.repository.AccountRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;

import com.banking.Banking_System.entities.Transaction;
import com.banking.Banking_System.entities.User;
import com.banking.Banking_System.service.AccountService;
import com.banking.Banking_System.service.TransactionService;
import com.banking.Banking_System.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    UserService userService;
    @Autowired
    TransactionService txnService;
    @Autowired
    AccountService accService;
    @Autowired
    private AccountRepository accountRepository;


    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        model.addAttribute("account", user.getAccount());
        return "customer/dashboard";
    }

    @GetMapping("/transfer")
    public String transferPage(Model model) {
        model.addAttribute("txn", new Transaction());
        return "customer/transfer";
    }

    @PostMapping("/transfer")
    public String doTransfer(@RequestParam String toAccount, @RequestParam double amount,
                             Principal principal, HttpServletRequest req, Model model) {
        User user = userService.findByUsername(principal.getName()).get();
        String fromAccount = user.getAccount().getAccountNumber();
        try {
            txnService.transferFunds(fromAccount, toAccount, amount, user, req.getRemoteAddr());
            return "redirect:/customer/dashboard?txnsuccess";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "customer/transfer";
        }
    }

    // cache for current balence etc.
    @Cacheable(value = "accountBalances", key = "#userId")
    public Double getCachedBalance(Long userId) {
        // fetch account by userId and return balance
        Account account = accountRepository.findByUserId(userId);
        return account != null ? account.getBalance() : 0.0;
    }

    // Transactions for the customer dashboard
    @GetMapping("/transactions")
    public String transactionHistory(Model model, Principal principal,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Page<Transaction> txnPage = txnService.getTransactionsByUserId(user.getId(), PageRequest.of(page <= 0 ? 0 : page, size));

        model.addAttribute("transactions", txnPage.getContent());  // List<Transaction>
        model.addAttribute("page", txnPage);                       // Page<Transaction> metadata for pagination

        return "customer/transactions";  // thyemleaf template name
    }

}
