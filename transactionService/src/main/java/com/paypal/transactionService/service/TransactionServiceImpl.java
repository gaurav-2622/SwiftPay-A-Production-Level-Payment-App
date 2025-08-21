package com.paypal.transactionService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.transactionService.entity.Transaction;
import com.paypal.transactionService.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public Transaction createTransaction(Transaction request) {

        System.out.println("🛩️ Entered createTransaction()");

        Long senderId = request.getSenderId();
        Long receiverId  = request.getReceiverId();
        Double amount =  request.getAmount();

        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        System.out.println("⚠️ Incoming Transaction Object: "+transaction);

        Transaction saved = transactionRepository.save(transaction);
        System.out.println("🤖 saved Transaction from DB:" + saved);


        return saved;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
