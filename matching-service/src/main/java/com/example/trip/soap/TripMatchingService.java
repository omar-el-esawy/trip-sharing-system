package com.example.trip.soap;

import com.example.trip.TripDTO;
import com.example.trip.TripSubmissionResult;
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import com.example.trip.TripOperationResponse;

@WebService
public interface TripMatchingService {

    @WebMethod
    TripOperationResponse submitTrip(TripDTO trip);

    @WebMethod
    TripDTO getTrip(String tripId);

    @WebMethod
    TripOperationResponse updateTrip(TripDTO trip);

    @WebMethod
    TripSubmissionResult deleteTrip(String tripId);
}
