package com.banking.Banking_System.controller;

import com.banking.Banking_System.entities.AuditLog;
import com.banking.Banking_System.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/logs")
    public String viewLogs(Model model) {
        List<AuditLog> logs = auditLogService.getAllLogs();  // returns list ordered by timestamp desc
        model.addAttribute("logs", logs);
        return "manager/logs";
    }

}

