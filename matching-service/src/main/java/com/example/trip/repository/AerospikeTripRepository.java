package com.example.trip.repository;

import com.aerospike.client.*;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.example.trip.TripDTO;
import org.example.YamlInjector;
import org.example.YamlValue;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class AerospikeTripRepository {

    @YamlValue(key = "aerospike.namespace")
    private String namespace;

    @YamlValue(key = "aerospike.setName")
    private String setName;

    @YamlValue(key = "aerospike.tripIdPrefix")
    private String tripIdPrefix;

    @YamlValue(key = "aerospike.host")
    private String host;

    @YamlValue(key = "aerospike.port")
    private int port;

    private static long tripIdCounter = System.currentTimeMillis();
    private final AerospikeClient client;

    public AerospikeTripRepository() {
        YamlInjector.inject(this);
        this.client = new AerospikeClient(host, port);
    }

    public String generateTripId() {
        synchronized (AerospikeTripRepository.class) {
            return tripIdPrefix + (++tripIdCounter);
        }
    }

    public TripDTO save(TripDTO trip) {
        // Auto-generate tripId if not set
        if (trip.getTripId() == null || trip.getTripId().isEmpty()) {
            trip.setTripId(generateTripId());
        }

        WritePolicy wp = new WritePolicy();
        wp.sendKey = true;

        Key key = new Key(namespace, setName, trip.getTripId());

        Bin userId = new Bin("userId", trip.getUserId());
        Bin userEmail = new Bin("userEmail", trip.getUserEmail());
        Bin startLat = new Bin("startLat", trip.getStartLat());
        Bin startLng = new Bin("startLng", trip.getStartLng());
        Bin endLat = new Bin("endLat", trip.getEndLat());
        Bin endLng = new Bin("endLng", trip.getEndLng());
        Bin travelMode = new Bin("travelMode", trip.getTravelMode());
        Bin startTime = new Bin("startTime", trip.getStartTime().toString());
        Bin matched = new Bin("matched", trip.isMatched());
        client.put(wp, key, userId, userEmail, startLat, startLng, endLat, endLng, travelMode, startTime, matched);

        return trip;
    }

    public void markAsMatched(String tripId) {
        Key key = new Key(namespace, setName, tripId);
        Bin matchedBin = new Bin("matched", true);
        client.put(null, key, matchedBin);
    }

    public List<TripDTO> findAllUnmatchedExcluding(String excludedTripId) {
        List<TripDTO> unmatchedTrips = new ArrayList<>();

        ScanCallback callback = (key, record) -> {
            if (key.userKey == null) {
                System.out.println("Skipping key with null userKey: " + key);
                // Skip keys that do not have a userKey of type String
                return;
            }

            String currentTripId = key.userKey.toString();

            // Skip the trip that triggered this match
            if (currentTripId.equals(excludedTripId)) return;

            boolean matched = Boolean.TRUE.equals(record.getBoolean("matched"));
            if (!matched) {
                TripDTO trip = new TripDTO();
                trip.setTripId(currentTripId);
                trip.setUserId(record.getString("userId"));
                trip.setUserEmail(record.getString("userEmail"));
                trip.setStartLat(record.getDouble("startLat"));
                trip.setStartLng(record.getDouble("startLng"));
                trip.setEndLat(record.getDouble("endLat"));
                trip.setEndLng(record.getDouble("endLng"));
                trip.setTravelMode(record.getString("travelMode"));
                trip.setStartTime(parse(record.getString("startTime")));
                trip.setMatched(false); // we already checked it is unmatched
                unmatchedTrips.add(trip);
            }
        };

        client.scanAll(null, namespace, setName, callback);
        return unmatchedTrips;
    }

    public TripDTO findById(String tripId) {
        Key key = new Key(namespace, setName, tripId);
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
        trip.setStartTime(parse(record.getString("startTime")));
        trip.setMatched(Boolean.TRUE.equals(record.getBoolean("matched")));
        return trip;
    }

    private static XMLGregorianCalendar parse(String iso) {
        ZonedDateTime zdt = ZonedDateTime.parse(iso, DateTimeFormatter.ISO_DATE_TIME);
        GregorianCalendar gc = GregorianCalendar.from(zdt);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public TripDTO update(TripDTO trip) {
        return save(trip);
    }

    public void delete(String tripId) {
        Key key = new Key(namespace, setName, tripId);
        client.delete(null, key);
    }
}
