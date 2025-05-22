package com.example.trip.soap;


import com.example.trip.ObjectFactory;
import com.example.trip.TripDTO;
import com.example.trip.TripSubmissionResult;
import jakarta.jws.WebService;

import java.util.concurrent.ConcurrentHashMap;

@WebService(endpointInterface = "com.example.trip.soap.TripMatchingService")
public class TripMatchingServiceImpl implements TripMatchingService {

    private final ConcurrentHashMap<String, TripDTO> db = new ConcurrentHashMap<>();

    public TripSubmissionResult submitTrip(TripDTO trip) {

        db.put(trip.getTripId(), trip);
        TripSubmissionResult tripSubmissionResult = new TripSubmissionResult();
        tripSubmissionResult.setSuccess(true);
        tripSubmissionResult.setMessage("Trip created");

        System.out.println("Trip in submit request created: " + trip.getTripId());
        return tripSubmissionResult;

    }

    public TripDTO getTrip(String tripId) {

        System.out.println("Trip in get request: " + tripId);

        return db.get(tripId);

    }

    public TripSubmissionResult updateTrip(TripDTO trip) {
        TripSubmissionResult tripSubmissionResult = new TripSubmissionResult();

        if (!db.containsKey(trip.getTripId())) {
            tripSubmissionResult.setSuccess(false);
            tripSubmissionResult.setMessage("Trip not found");
            System.out.println("Trip in update request not found: " + trip.getTripId());
        } else {
            tripSubmissionResult.setSuccess(true);
            tripSubmissionResult.setMessage("Trip updated");
            db.put(trip.getTripId(), trip);
            System.out.println("Trip in update request updated: " + trip.getTripId());
        }
        return tripSubmissionResult;
    }

    public TripSubmissionResult deleteTrip(String tripId) {
        TripSubmissionResult tripSubmissionResult = new TripSubmissionResult();
        if (!db.containsKey(tripId)) {
            tripSubmissionResult.setSuccess(false);
            tripSubmissionResult.setMessage("Trip not found");
            System.out.println("Trip in delete request not found: " + tripId);
        } else {
            tripSubmissionResult.setSuccess(true);
            tripSubmissionResult.setMessage("Trip deleted");
            db.remove(tripId);
            System.out.println("Trip in delete request deleted: " + tripId);
        }
        return tripSubmissionResult;
    }
}
