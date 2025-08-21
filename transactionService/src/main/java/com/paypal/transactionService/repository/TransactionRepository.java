package com.paypal.transactionService.repository;

import com.paypal.transactionService.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction , Long> {
}
