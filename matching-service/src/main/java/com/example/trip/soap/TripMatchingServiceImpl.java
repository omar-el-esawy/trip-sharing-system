package com.example.trip.soap;


import com.example.trip.TripDTO;
import com.example.trip.TripSubmissionResult;
import jakarta.jws.WebService;

import java.util.concurrent.ConcurrentHashMap;

@WebService(endpointInterface = "com.example.trip.soap.TripMatchingService")
public class TripMatchingServiceImpl implements TripMatchingService {

    private final ConcurrentHashMap<String, TripDTO> db = new ConcurrentHashMap<>();

    public TripSubmissionResult submitTrip(TripDTO trip) {
        db.put(trip.getTripId(), trip);
        return new TripSubmissionResult(true, "Trip created");
    }

    public TripDTO getTrip(String tripId) {
        return db.get(tripId);
    }

    public TripSubmissionResult updateTrip(TripDTO trip) {
        if (!db.containsKey(trip.getTripId())) {
            return new TripSubmissionResult(false, "Trip not found");
        }
        db.put(trip.getTripId(), trip);
        return new TripSubmissionResult(true, "Trip updated");
    }

    public TripSubmissionResult deleteTrip(String tripId) {
        return db.remove(tripId) != null
                ? new TripSubmissionResult(true, "Deleted")
                : new TripSubmissionResult(false, "Trip not found");
    }
}
