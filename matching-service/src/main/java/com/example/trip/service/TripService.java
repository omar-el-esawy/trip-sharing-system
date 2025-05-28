package com.example.trip.service;

import com.example.trip.TripDTO;
import com.example.trip.TripSubmissionResult;
import com.example.trip.TripOperationResponse;
import com.example.trip.repository.AerospikeTripRepository;

public class TripService {

    private final AerospikeTripRepository repository;

    public TripService(AerospikeTripRepository repository) {
        this.repository = repository;
    }

    public TripOperationResponse submitTrip(TripDTO trip) {
        trip.setTripId(repository.generateTripId());
        repository.save(trip);
        TripOperationResponse response = new TripOperationResponse();
        response.setMessage("Trip created");
        response.setTrip(trip);
        return response;
    }

    public TripDTO getTrip(String tripId) {
        return repository.findById(tripId);
    }

    public TripOperationResponse updateTrip(TripDTO trip) {
        TripOperationResponse response = new TripOperationResponse();
        if (repository.findById(trip.getTripId()) == null) {
            response.setMessage("Trip not found");
            response.setTrip(null);
        } else {
            repository.update(trip);
            response.setMessage("Trip updated");
            response.setTrip(trip);
        }
        return response;
    }

    public TripSubmissionResult deleteTrip(String tripId) {
        TripSubmissionResult result = new TripSubmissionResult();
        if (repository.findById(tripId) == null) {
            result.setSuccess(false);
            result.setMessage("Trip not found");
        } else {
            repository.delete(tripId);
            result.setSuccess(true);
            result.setMessage("Trip deleted");
        }
        return result;
    }
}
