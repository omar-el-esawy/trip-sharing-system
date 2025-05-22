package com.example.trip.soap;

import com.example.trip.TripDTO;
import com.example.trip.TripSubmissionResult;
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;


@WebService
public interface TripMatchingService {

    @WebMethod
    TripSubmissionResult submitTrip(TripDTO trip);

    @WebMethod
    TripDTO getTrip(String tripId);

    @WebMethod
    TripSubmissionResult updateTrip(TripDTO trip);

    @WebMethod
    TripSubmissionResult deleteTrip(String tripId);
}
