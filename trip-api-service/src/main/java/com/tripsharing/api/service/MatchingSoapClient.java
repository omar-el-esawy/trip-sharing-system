package com.tripsharing.api.service;

import com.client.generated.*;

import java.net.URL;
import org.example.YamlInjector;
import org.example.YamlValue;

public class MatchingSoapClient {

    @YamlValue(key = "soap.wsdlUrl")
    private String wsdlUrl;

    private final TripMatchingService servicePort;

    public MatchingSoapClient() throws Exception {
        YamlInjector.inject(this);
        TripMatchingServiceImplService service = new TripMatchingServiceImplService(new URL(wsdlUrl));
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
