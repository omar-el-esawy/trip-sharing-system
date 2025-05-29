package com.example.trip;

import com.example.trip.kafka.TripMatchedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationServiceApplication {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceApplication.class);

    public static void main(String[] args) {
        Thread consumerThread = new Thread(new TripMatchedConsumer());
        consumerThread.setDaemon(true);
        consumerThread.start();

        logger.info("Notification Service started and listening for trip-matched events...");

        // Block forever to keep app alive
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            logger.error("Main thread interrupted", e);
        }
    }
}
