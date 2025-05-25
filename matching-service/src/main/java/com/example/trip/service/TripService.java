package com.example.trip.service;

import com.example.trip.TripDTO;
import com.example.trip.TripSubmissionResult;
import com.example.trip.repository.AerospikeTripRepository;

public class TripService {

    private final AerospikeTripRepository repository;

    public TripService(AerospikeTripRepository repository) {
        this.repository = repository;
    }

    public TripSubmissionResult submitTrip(TripDTO trip) {
        repository.save(trip);
        TripSubmissionResult result = new TripSubmissionResult();
        result.setSuccess(true);
        result.setMessage("Trip created");
        return result;
    }

    public TripDTO getTrip(String tripId) {
        return repository.findById(tripId);
    }

    public TripSubmissionResult updateTrip(TripDTO trip) {
        TripSubmissionResult result = new TripSubmissionResult();
        if (repository.findById(trip.getTripId()) == null) {
            result.setSuccess(false);
            result.setMessage("Trip not found");
        } else {
            repository.update(trip);
            result.setSuccess(true);
            result.setMessage("Trip updated");
        }
        return result;
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
