package com.example.trip.soap;

import com.example.trip.TripDTO;
import com.example.trip.TripSubmissionResult;
import com.example.trip.TripOperationResponse;
import jakarta.jws.WebService;
import com.example.trip.service.TripService;
import com.example.trip.repository.AerospikeTripRepository;

@WebService(endpointInterface = "com.example.trip.soap.TripMatchingService")
public class TripMatchingServiceImpl implements TripMatchingService {

    private final TripService tripService;

    public TripMatchingServiceImpl() {
        // In production, use dependency injection or a factory
        this.tripService = new TripService(
                new AerospikeTripRepository());
    }

    public TripOperationResponse submitTrip(TripDTO trip) {
        return tripService.submitTrip(trip);
    }

    public TripDTO getTrip(String tripId) {
        return tripService.getTrip(tripId);
    }

    public TripOperationResponse updateTrip(TripDTO trip) {
        return tripService.updateTrip(trip);
    }

    public TripSubmissionResult deleteTrip(String tripId) {
        return tripService.deleteTrip(tripId);
    }
}
