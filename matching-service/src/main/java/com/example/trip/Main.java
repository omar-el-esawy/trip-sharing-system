package com.example.trip;

import com.example.trip.soap.TripMatchingServiceImpl;
import jakarta.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8082/ws/trip", new TripMatchingServiceImpl());
        System.out.println("🚀 Matching SOAP Service running on http://localhost:8082/ws/trip?wsdl");
    }
}