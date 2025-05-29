package com.tripsharing.api;

import com.tripsharing.api.config.JerseyConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import java.net.URI;
import org.example.YamlInjector;
import org.example.YamlValue;

public class TripApiServiceApplication {

    @YamlValue(key = "api.baseUri")
    private static String baseUri;

    public static void main(String[] args) {
        YamlInjector.inject(TripApiServiceApplication.class);
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), new JerseyConfig());
        System.out.printf("🚀 Trip API Service started at %s%nPress Ctrl+C to stop.%n", baseUri);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            server.shutdownNow();
        }
    }
}
