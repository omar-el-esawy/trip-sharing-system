package com.tripsharing.api.resource;

import com.client.generated.TripDTO;
import com.client.generated.TripOperationResponse;
import com.client.generated.TripSubmissionResult;
import com.tripsharing.api.service.MatchingSoapClient;
import com.tripsharing.api.kafka.KafkaProducerUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/trips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TripResource {

    private static final MatchingSoapClient matchingSoapClient;

    static {
        try {
            matchingSoapClient = new MatchingSoapClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    public Response createTrip(TripDTO trip) {
        TripOperationResponse response = matchingSoapClient.submitTrip(trip);

        // Publish tripId to Kafka after successful SOAP call
        KafkaProducerUtil.sendTripScheduledEvent(response.getTrip().getTripId());

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{tripId}")
    public Response getTrip(@PathParam("tripId") String tripId) {
        TripDTO trip = matchingSoapClient.getTrip(tripId);
        return Response.ok(trip).build();
    }

    @PUT
    @Path("/{tripId}")
    public Response updateTrip(@PathParam("tripId") String tripId, TripDTO trip) {
        trip.setTripId(tripId);
        TripOperationResponse response = matchingSoapClient.updateTrip(trip);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{tripId}")
    public Response deleteTrip(@PathParam("tripId") String tripId) {
        TripSubmissionResult tripSubmissionResult = matchingSoapClient.deleteTrip(tripId);
        if (tripSubmissionResult == null || !tripSubmissionResult.isSuccess()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Trip not found").build();
        }

        return Response.ok(tripSubmissionResult.getMessage()).build();
    }
}
