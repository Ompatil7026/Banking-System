package com.banking.Banking_System.controller;

import com.banking.Banking_System.entities.Account;
import com.banking.Banking_System.entities.User;
import com.banking.Banking_System.service.ReportPdfService;
import com.banking.Banking_System.service.UserService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import com.banking.Banking_System.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportPdfService reportPdfService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Example: Load all accounts or whatever the manager dashboard needs
        var accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);

        return "manager/dashboard";
    }

    //view accounts
    @GetMapping("/accounts")
    public String accounts(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountPage = accountService.getAccountsPage(pageable);

        model.addAttribute("accountPage", accountPage);
        return "manager/accounts";
    }

    //for approvals accounts.
    @GetMapping("approvals")
    public String approvals(Model model){
        var approvals=accountService.getAllAccounts();
        model.addAttribute("approvals",approvals);

        return "manager/approvals";
    }


    // Approve account
    @PostMapping("/approvals/approve/{id}")
    public String approveAccount(@PathVariable Long id, HttpServletRequest request, Principal principal) {
        String username = principal.getName();

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            // Handle user not found: for example, redirect with error message or throw exception
            throw new RuntimeException("Manager user not found: " + username);
        }
        User manager = userOpt.get();

        String ip = request.getRemoteAddr();

        accountService.approveAccount(id, manager, ip);

        return "redirect:/manager/approvals";
    }


    // Reject account
    @PostMapping("/approvals/reject/{id}")
    public String rejectAccount(@PathVariable Long id) {
        accountService.rejectAccount(id);
        return "redirect:/manager/approvals";
    }

    @GetMapping("/reports/download-pdf")
    public void downloadPdf(HttpServletResponse response) throws IOException, DocumentException {
        reportPdfService.generateBankingReportPdf(response);
    }

    @GetMapping("/reports")
    public String reportsPage() {
        return "manager/reports";  // should match your Thymeleaf file path
    }

}
