package com.tripsharing.api.service;

import com.client.generated.*;

import java.net.URL;

public class MatchingSoapClient {

    private static final String WSDL_URL = "http://localhost:8082/ws/trip?wsdl";

    private final TripMatchingService servicePort;

    public MatchingSoapClient() throws Exception {
        TripMatchingServiceImplService service = new TripMatchingServiceImplService(new URL(WSDL_URL));
        servicePort = service.getTripMatchingServiceImplPort();
    }

    public TripOperationResponse submitTrip(TripDTO trip) {
        return servicePort.submitTrip(trip);
    }

    public TripDTO getTrip(String tripId) {
        return servicePort.getTrip(tripId);
    }

    public TripOperationResponse updateTrip(TripDTO trip) {
        return servicePort.updateTrip(trip);
    }

    public TripSubmissionResult deleteTrip(String tripId) {
        return servicePort.deleteTrip(tripId);
    }
}
