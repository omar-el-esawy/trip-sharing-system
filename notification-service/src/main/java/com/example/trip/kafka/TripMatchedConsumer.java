package com.example.trip.kafka;

import com.example.trip.service.EmailSchedulerService;
import jakarta.mail.MessagingException;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

public class TripMatchedConsumer implements Runnable {
    private final KafkaConsumer<String, String> consumer;

    public TripMatchedConsumer() {
        this.consumer = new KafkaConsumer<>(KafkaConfig.consumerProps());
        this.consumer.subscribe(Collections.singletonList(KafkaConfig.MATCHED_TOPIC));
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            records.forEach(record -> {
                String tripId = record.value();
                sendEmail(tripId);
            });
        }
    }

    private void sendEmail(String tripOwnerEmail) {

        EmailSchedulerService emailService = new EmailSchedulerService();
        try {
            emailService.sendEmail(
                    tripOwnerEmail,
                    "Trip Matched Notification",
                    "Your trip has been successfully matched with another user."
            );
        } catch (MessagingException e) {
            System.err.println("Failed to send email for trip: " + tripOwnerEmail);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // Placeholder for email logic
        System.out.println("Sending email for matched trip: " + tripOwnerEmail);
    }
}
