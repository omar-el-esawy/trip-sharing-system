package com.example.trip.kafka;

import com.example.trip.repository.AerospikeTripRepository;
import com.example.trip.service.TripMatchingService;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.time.Duration;
import java.util.Collections;

// Import the matching service

public class TripScheduledConsumer implements Runnable {
    private final KafkaConsumer<String, String> consumer;
    private final AerospikeTripRepository tripRepository;
    private final TripMatchingService tripMatchingService;

    public TripScheduledConsumer() {
        this.consumer = new KafkaConsumer<>(KafkaConfig.consumerProps());
        this.consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC));
        this.tripRepository = new AerospikeTripRepository();
        this.tripMatchingService = new TripMatchingService(tripRepository);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                records.forEach(record -> {
                    String tripId = record.value();
                    tripMatchingService.matchTrip(tripId);
                });
            }
        } catch (Exception e) {
            System.err.println("❗ Consumer error: " + e.getMessage());
        } finally {
            System.out.println("🛑 Closing Kafka consumer...");
            consumer.close();
        }
    }
}
