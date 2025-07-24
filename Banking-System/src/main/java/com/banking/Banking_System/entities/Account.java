package com.banking.Banking_System.entities;

import jakarta.persistence.*;
import lombok.Data;

    @Entity
    @Table(name = "accounts")
    @Data
    public class Account {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String accountNumber, holderName, status;
        private double balance;

        @OneToOne
        @JoinColumn(name = "user_id")
        private User user;
    }

