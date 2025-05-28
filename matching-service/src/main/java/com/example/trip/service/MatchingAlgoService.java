package com.example.trip.service;

import com.example.trip.TripDTO;
import com.example.trip.repository.AerospikeTripRepository;
import com.example.trip.kafka.KafkaProducerUtil;

import java.util.List;
import java.util.Map;

public class MatchingAlgoService {

    private final AerospikeTripRepository tripRepository;
    private final double maxDistance;
    private final long maxTimeDiffSeconds;

    public MatchingAlgoService(AerospikeTripRepository tripRepository) {
        this.tripRepository = tripRepository;

        // Load matching config from application.yml using YamlConfigUtil
        double tempMaxDistance = 0.5;
        long tempMaxTimeDiffSeconds = 15 * 60;
        try (java.io.InputStream in = MatchingAlgoService.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (in != null) {
                org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
                Map<String, Object> obj = yaml.load(in);
                Map<String, Object> matching = (Map<String, Object>) obj.get("matching");
                if (matching != null) {
                    if (matching.get("maxDistance") != null) {
                        tempMaxDistance = ((Number) matching.get("maxDistance")).doubleValue();
                    }
                    if (matching.get("maxTimeDiffSeconds") != null) {
                        tempMaxTimeDiffSeconds = ((Number) matching.get("maxTimeDiffSeconds")).longValue();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load matching config from application.yml, using defaults.");
        }
        this.maxDistance = tempMaxDistance;
        this.maxTimeDiffSeconds = tempMaxTimeDiffSeconds;
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
