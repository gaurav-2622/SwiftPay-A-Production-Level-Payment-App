package com.paypal.transactionService.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.transactionService.entity.Transaction;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class kafkaEventProducer {

    private static final String TOPIC = "txn-initiated";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendTransactionEvent(String key,  String  message){
        System.out.println("📤 Sending to Kafka → Topic: " + TOPIC + ", Key: " + key + ", Message: " + message);

        CompletableFuture<SendResult<String,String>> future = kafkaTemplate.send(TOPIC,key, message);

        future.thenAccept(result ->{
            RecordMetadata metadata= result.getRecordMetadata();
            System.out.println("✅ Kafka message sent successfully! Topic: " + metadata.topic() + ", Partition: " +
                    metadata.partition() + ", Offset: " + metadata.offset());
        }).exceptionally(ex -> {
            System.err.println("❌ Failed to send Kafka message: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        });
    }

    public void sendTransactionEvent(String key, Transaction transaction){
        try {
            String message = objectMapper.writeValueAsString(transaction);
            sendTransactionEvent(key,message);
        }catch (JsonProcessingException e){
            System.err.println("❌ Error serializing transaction: " +e.getMessage());
        }
    }

}
