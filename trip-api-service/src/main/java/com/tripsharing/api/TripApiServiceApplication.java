package com.tripsharing.api;

import com.tripsharing.api.config.JerseyConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import java.net.URI;
import org.example.YamlInjector;
import org.example.YamlValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TripApiServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(TripApiServiceApplication.class);

    @YamlValue(key = "api.baseUri")
    private static String baseUri;

    public static void main(String[] args) {
        YamlInjector.inject(TripApiServiceApplication.class);
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), new JerseyConfig());
        logger.info("🚀 Trip API Service started at {}\nPress Ctrl+C to stop.", baseUri);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            server.shutdownNow();
        }
    }
}
