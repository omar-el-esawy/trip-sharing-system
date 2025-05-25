package com.example.trip.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

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

    private void sendEmail(String tripId) {
        // Placeholder for email logic
        System.out.println("Sending email for matched trip: " + tripId);
    }
}
