package com.example.trip.service;

import com.example.trip.TripDTO;
import com.example.trip.repository.AerospikeTripRepository;
import com.example.trip.kafka.KafkaProducerUtil;
import org.example.YamlInjector;
import org.example.YamlValue;

import java.util.List;

public class MatchingAlgoService {

    private final AerospikeTripRepository tripRepository;

    @YamlValue(key = "matching.maxDistance")
    private double maxDistance;

    @YamlValue(key = "matching.maxTimeDiffSeconds")
    private long maxTimeDiffSeconds;

    public MatchingAlgoService(AerospikeTripRepository tripRepository) {
        this.tripRepository = tripRepository;
        YamlInjector.inject(this);
    }

    public void matchTrip(String tripId) {
        TripDTO newTrip = tripRepository.findById(tripId);
        if (newTrip == null || newTrip.isMatched()) {
            System.out.println("❌ Trip not found or already matched: " + tripId);
            return;
        }

        System.out.println("🔍 Matching tripId: " + tripId);

        // Fetch all unmatched trips excluding this one
        List<TripDTO> unmatchedTrips = tripRepository.findAllUnmatchedExcluding(tripId);

        for (TripDTO candidate : unmatchedTrips) {
            if (isMatch(newTrip, candidate)) {
                // ✅ Mark both trips as matched
                tripRepository.markAsMatched(tripId);
                tripRepository.markAsMatched(candidate.getTripId());

                // ✅ Send trip-matched Kafka events
                KafkaProducerUtil.sendTripMatchedEvent(newTrip.getUserEmail());
                KafkaProducerUtil.sendTripMatchedEvent(candidate.getUserEmail());

                System.out.println("✅ Matched " + tripId + " with " + candidate.getTripId());
                return;
            }
        }

        System.out.println("⚠️ No match found for tripId: " + tripId);
    }

    private boolean isMatch(TripDTO a, TripDTO b) {
        boolean closeStart = distance(a.getStartLat(), a.getStartLng(), b.getStartLat(), b.getStartLng()) < maxDistance;
        boolean timeClose = Math.abs(
                a.getStartTime().toGregorianCalendar().toInstant().getEpochSecond() -
                        b.getStartTime().toGregorianCalendar().toInstant().getEpochSecond()
        ) < maxTimeDiffSeconds;

        return closeStart && timeClose;
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lng1 - lng2, 2));
    }
}
