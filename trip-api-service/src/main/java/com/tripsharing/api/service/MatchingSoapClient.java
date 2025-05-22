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

    public TripSubmissionResult submitTrip(TripDTO trip) {
//        SubmitTrip request = new SubmitTrip();
//        request.setArg0(trip);
//        SubmitTripResponse response = servicePort.submitTrip(trip);
        return servicePort.submitTrip(trip);
    }

    public TripDTO getTrip(String tripId) {
//        GetTrip request = new GetTrip();
//        request.setArg0(tripId);
//        GetTripResponse response = servicePort.getTrip(request);
        return servicePort.getTrip(tripId);
    }

    public TripSubmissionResult updateTrip(TripDTO trip) {
//        UpdateTrip request = new UpdateTrip();
//        request.setArg0(trip);
//        UpdateTripResponse response = servicePort.updateTrip(request);
        return servicePort.updateTrip(trip);
    }

    public TripSubmissionResult deleteTrip(String tripId) {
//        DeleteTrip request = new DeleteTrip();
//        request.setArg0(tripId);
//        DeleteTripResponse response = servicePort.deleteTrip(request);
        return servicePort.deleteTrip(tripId);
    }
}
