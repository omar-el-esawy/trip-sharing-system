package com.example.trip;

import com.example.trip.kafka.KafkaConfig;
import com.example.trip.soap.TripMatchingServiceImpl;
import com.example.trip.kafka.TripScheduledConsumer;
import jakarta.xml.ws.Endpoint;
import org.example.YamlInjector;
import org.example.YamlValue;

public class MatchingServiceApplication {

    @YamlValue(key = "soap.endpointUrl")
    private static String endpointUrl;

    public static void main(String[] args) {
        YamlInjector.inject(MatchingServiceApplication.class);

        // Start Kafka consumer in background
        Thread consumerThread = new Thread(new TripScheduledConsumer());
        consumerThread.setDaemon(true);
        consumerThread.start();

        Endpoint.publish(endpointUrl, new TripMatchingServiceImpl());
        System.out.println("🚀 Matching SOAP Service running on " + endpointUrl + "?wsdl");
    }
}
