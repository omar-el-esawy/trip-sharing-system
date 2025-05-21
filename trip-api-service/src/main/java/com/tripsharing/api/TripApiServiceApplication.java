package com.tripsharing.api;

import com.tripsharing.api.config.JerseyConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import java.net.URI;

public class TripApiServiceApplication {
    public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) {
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new JerseyConfig());
        System.out.printf("🚀 Trip API Service started at %s%nPress Ctrl+C to stop.%n", BASE_URI);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            server.shutdownNow();
        }
    }
}
