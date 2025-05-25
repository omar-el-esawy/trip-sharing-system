package com.example.trip.repository;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.example.trip.TripDTO;

import java.time.OffsetDateTime;

public class AerospikeTripRepository {

    private static final String NAMESPACE = "test";
    private static final String SET_NAME = "trips";
    private final AerospikeClient client;

    public AerospikeTripRepository() {
        this.client = new AerospikeClient("localhost", 3000);
    }

    public void save(TripDTO trip) {
        Key key = new Key(NAMESPACE, SET_NAME, trip.getTripId());
        Bin userId = new Bin("userId", trip.getUserId());
        Bin userEmail = new Bin("userEmail", trip.getUserEmail());
        Bin startLat = new Bin("startLat", trip.getStartLat());
        Bin startLng = new Bin("startLng", trip.getStartLng());
        Bin endLat = new Bin("endLat", trip.getEndLat());
        Bin endLng = new Bin("endLng", trip.getEndLng());
        Bin travelMode = new Bin("travelMode", trip.getTravelMode());
        Bin startTime = new Bin("startTime", trip.getStartTime().toString());
        client.put(new WritePolicy(), key, userId, userEmail, startLat, startLng, endLat, endLng, travelMode, startTime);
    }

    public TripDTO findById(String tripId) {
        Key key = new Key(NAMESPACE, SET_NAME, tripId);
        Record record = client.get(null, key);
        if (record == null) return null;
        TripDTO trip = new TripDTO();
        trip.setTripId(tripId);
        trip.setUserId(record.getString("userId"));
        trip.setUserEmail(record.getString("userEmail"));
        trip.setStartLat(record.getDouble("startLat"));
        trip.setStartLng(record.getDouble("startLng"));
        trip.setEndLat(record.getDouble("endLat"));
        trip.setEndLng(record.getDouble("endLng"));
        trip.setTravelMode(record.getString("travelMode"));
        trip.setStartTime(OffsetDateTime.parse(record.getString("startTime")).toLocalDateTime());
        return trip;
    }

    public void update(TripDTO trip) {
        save(trip);
    }

    public void delete(String tripId) {
        Key key = new Key(NAMESPACE, SET_NAME, tripId);
        client.delete(null, key);
    }
}
