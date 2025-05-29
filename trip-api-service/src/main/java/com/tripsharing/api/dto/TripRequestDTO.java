package com.tripsharing.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripRequestDTO {

    private String tripId;

    private String userId;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotNull
    @DecimalMin(value = "-90.0", message = "Start latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Start latitude must be <= 90")
    private Double startLat;

    @NotNull
    @DecimalMin(value = "-180.0", message = "Start longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Start longitude must be <= 180")
    private Double startLng;

    @NotNull
    @DecimalMin(value = "-90.0", message = "End latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "End latitude must be <= 90")
    private Double endLat;

    @NotNull
    @DecimalMin(value = "-180.0", message = "End longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "End longitude must be <= 180")
    private Double endLng;

    private boolean matched;


    private String travelMode;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;
}
