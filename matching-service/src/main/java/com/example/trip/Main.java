package com.example.trip;

import com.example.trip.soap.TripMatchingServiceImpl;
import com.example.trip.kafka.TripScheduledConsumer;
import jakarta.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        // Start Kafka consumer in background
        Thread consumerThread = new Thread(new TripScheduledConsumer());
        consumerThread.setDaemon(true);
        consumerThread.start();

        Endpoint.publish("http://localhost:8082/ws/trip", new TripMatchingServiceImpl());
        System.out.println("🚀 Matching SOAP Service running on http://localhost:8082/ws/trip?wsdl");
    }
}