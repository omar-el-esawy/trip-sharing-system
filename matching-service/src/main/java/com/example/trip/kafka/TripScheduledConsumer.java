package com.example.trip.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

public class TripScheduledConsumer implements Runnable {
    private final KafkaConsumer<String, String> consumer;

    public TripScheduledConsumer() {
        this.consumer = new KafkaConsumer<>(KafkaConfig.consumerProps());
        this.consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC));
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            records.forEach(record -> {
                String tripId = record.value();
                runMatchingAlgorithm(tripId);
            });
        }
    }

    private void runMatchingAlgorithm(String tripId) {
        // Placeholder for matching logic
        System.out.println("Running matching algorithm for tripId: " + tripId);
    }
}
