package com.banking.Banking_System.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private double amount;
    private LocalDateTime timestamp;
    private String status;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
