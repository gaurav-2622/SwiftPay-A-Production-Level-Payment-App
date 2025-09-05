package com.paypal.transactionService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.transactionService.entity.Transaction;
import com.paypal.transactionService.kafka.kafkaEventProducer;
import com.paypal.transactionService.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private kafkaEventProducer kafkaEventProducer;

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

        try{
            String eventPayLoad = objectMapper.writeValueAsString(saved);
            String key = String.valueOf(saved.getId());
            kafkaEventProducer.sendTransactionEvent(key, eventPayLoad);
            System.out.println("🚀 Kafka message sent");
        }catch (Exception e){
            System.err.println("❌ Failed to send Kafka event: " + e.getMessage());
            e.printStackTrace();
        }
        return saved;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
