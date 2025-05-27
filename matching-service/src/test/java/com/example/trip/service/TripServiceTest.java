package com.example.trip.service;

import com.example.trip.TripDTO;
import com.example.trip.TripSubmissionResult;
import com.example.trip.repository.AerospikeTripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TripServiceTest {

    private AerospikeTripRepository repository;
    private TripService tripService;

    @BeforeEach
    void setUp() {
        repository = mock(AerospikeTripRepository.class);
        tripService = new TripService(repository);
    }

    @Test
    void submitTrip_shouldSaveTripAndReturnSuccess() {
        TripDTO trip = new TripDTO();
        TripSubmissionResult result = tripService.submitTrip(trip);

        verify(repository).save(trip);
        assertTrue(result.isSuccess());
        assertEquals("Trip created", result.getMessage());
    }

    @Test
    void getTrip_shouldReturnTripFromRepository() {
        TripDTO trip = new TripDTO();
        when(repository.findById("id1")).thenReturn(trip);

        TripDTO result = tripService.getTrip("id1");
        assertEquals(trip, result);
        verify(repository).findById("id1");
    }

    @Test
    void updateTrip_shouldReturnErrorIfTripNotFound() {
        TripDTO trip = new TripDTO();
        trip.setTripId("id2");
        when(repository.findById("id2")).thenReturn(null);

        TripSubmissionResult result = tripService.updateTrip(trip);

        assertFalse(result.isSuccess());
        assertEquals("Trip not found", result.getMessage());
        verify(repository).findById("id2");
        verify(repository, never()).update(any());
    }

    @Test
    void updateTrip_shouldUpdateIfTripExists() {
        TripDTO trip = new TripDTO();
        trip.setTripId("id3");
        when(repository.findById("id3")).thenReturn(trip);

        TripSubmissionResult result = tripService.updateTrip(trip);

        assertTrue(result.isSuccess());
        assertEquals("Trip updated", result.getMessage());
        verify(repository).findById("id3");
        verify(repository).update(trip);
    }

    @Test
    void deleteTrip_shouldReturnErrorIfTripNotFound() {
        when(repository.findById("id4")).thenReturn(null);

        TripSubmissionResult result = tripService.deleteTrip("id4");

        assertFalse(result.isSuccess());
        assertEquals("Trip not found", result.getMessage());
        verify(repository).findById("id4");
        verify(repository, never()).delete(any());
    }

    @Test
    void deleteTrip_shouldDeleteIfTripExists() {
        TripDTO trip = new TripDTO();
        when(repository.findById("id5")).thenReturn(trip);

        TripSubmissionResult result = tripService.deleteTrip("id5");

        assertTrue(result.isSuccess());
        assertEquals("Trip deleted", result.getMessage());
        verify(repository).findById("id5");
        verify(repository).delete("id5");
    }
}
