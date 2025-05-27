package com.example.trip;

import com.example.trip.kafka.TripMatchedConsumer;

public class NotificationServiceApplication {
    public static void main(String[] args) {
        Thread consumerThread = new Thread(new TripMatchedConsumer());
        consumerThread.setDaemon(true);
        consumerThread.start();

        System.out.println("Notification Service started and listening for trip-matched events...");

        // Block forever to keep app alive
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
