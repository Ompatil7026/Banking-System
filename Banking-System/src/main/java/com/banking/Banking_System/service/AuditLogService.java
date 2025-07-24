package com.banking.Banking_System.service;

import com.banking.Banking_System.entities.AuditLog;
import com.banking.Banking_System.entities.User;
import com.banking.Banking_System.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository repo;

    public void logAction(User user, String action, String ip) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setIp(ip);
        log.setTimestamp(LocalDateTime.now());
        log.setUserRole(user.getRole());
        log.setUser(user);
        repo.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return repo.findAllByOrderByTimestampDesc();
    }

    // Returns the most recent logs, limited by count
    public List<AuditLog> getRecentLogs(int limit) {
        return repo.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "timestamp"))
        ).getContent();
    }

}
