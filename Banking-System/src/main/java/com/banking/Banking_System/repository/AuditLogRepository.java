package com.banking.Banking_System.repository;

import com.banking.Banking_System.entities.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByTimestampDesc();

    // In AuditLogRepository:
    Page<AuditLog> findAll(Pageable pageable);

}
