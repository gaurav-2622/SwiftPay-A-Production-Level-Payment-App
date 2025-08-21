package com.paypal.transactionService.controller;

import com.paypal.transactionService.entity.Transaction;
import com.paypal.transactionService.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<Transaction> create(@Valid @RequestBody Transaction transaction){
        Transaction created = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @GetMapping("/all")
    public  ResponseEntity<List<Transaction>> getAll(){
        return new ResponseEntity<>(transactionService.getAllTransactions(),HttpStatus.OK);
    }
}
