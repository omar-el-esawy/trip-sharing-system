package com.tripsharing.api.service;

import com.client.generated.TripDTO;
import com.client.generated.TripOperationResponse;
import com.client.generated.TripSubmissionResult;
import com.tripsharing.api.dto.TripRequestDTO;
import com.tripsharing.api.mapper.TripRequestMapper;
import com.tripsharing.api.kafka.KafkaProducerUtil;
import com.tripsharing.api.soap.MatchingSoapClient;

public class TripService {

    private final MatchingSoapClient matchingSoapClient;

    public TripService() {
        try {
            this.matchingSoapClient = new MatchingSoapClient();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MatchingSoapClient", e);
        }
    }

    public TripOperationResponse createTrip(TripRequestDTO tripRequestDTO) {
        TripDTO tripDTO = TripRequestMapper.INSTANCE.toTripDTO(tripRequestDTO);
        TripOperationResponse response = matchingSoapClient.submitTrip(tripDTO);

        // Send Kafka event after successful trip creation
        if (response != null && response.getTrip() != null && response.getTrip().getTripId() != null) {
            KafkaProducerUtil.sendTripScheduledEvent(response.getTrip().getTripId());
        }
        return response;
    }

    public TripDTO getTrip(String tripId) {
        return matchingSoapClient.getTrip(tripId);
    }

    public TripOperationResponse updateTrip(String tripId, TripRequestDTO tripRequestDTO) {
        TripDTO tripDTO = TripRequestMapper.INSTANCE.toTripDTO(tripRequestDTO);
        tripDTO.setTripId(tripId);
        return matchingSoapClient.updateTrip(tripDTO);
    }

    public TripSubmissionResult deleteTrip(String tripId) {
        return matchingSoapClient.deleteTrip(tripId);
    }
}
