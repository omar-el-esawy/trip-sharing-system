package com.tripsharing.api.dto;

import java.time.LocalDateTime;

public class TripDTO {
    private String tripId;
    private String userId;
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private String travelMode;
    private LocalDateTime startTime;

    public TripDTO() {}

    public TripDTO(String tripId, String userId, double startLat, double startLng, double endLat, double endLng, String travelMode, LocalDateTime startTime) {
        this.tripId = tripId;
        this.userId = userId;
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.travelMode = travelMode;
        this.startTime = startTime;
    }

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getStartLat() { return startLat; }
    public void setStartLat(double startLat) { this.startLat = startLat; }

    public double getStartLng() { return startLng; }
    public void setStartLng(double startLng) { this.startLng = startLng; }

    public double getEndLat() { return endLat; }
    public void setEndLat(double endLat) { this.endLat = endLat; }

    public double getEndLng() { return endLng; }
    public void setEndLng(double endLng) { this.endLng = endLng; }

    public String getTravelMode() { return travelMode; }
    public void setTravelMode(String travelMode) { this.travelMode = travelMode; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
}
