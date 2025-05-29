package com.tripsharing.api.controller;

import com.client.generated.TripDTO;
import com.client.generated.TripOperationResponse;
import com.client.generated.TripSubmissionResult;
import com.tripsharing.api.dto.TripRequestDTO;
import com.tripsharing.api.service.TripService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/trips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TripController {

    private static final TripService tripService = new TripService();

    @POST
    public Response createTrip(@Valid TripRequestDTO tripRequestDTO) {
        TripOperationResponse response = tripService.createTrip(tripRequestDTO);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{tripId}")
    public Response getTrip(@PathParam("tripId") String tripId) {
        TripDTO trip = tripService.getTrip(tripId);
        return Response.ok(trip).build();
    }

    @PUT
    @Path("/{tripId}")
    public Response updateTrip(@PathParam("tripId") String tripId, @Valid TripRequestDTO tripRequestDTO) {
        TripOperationResponse response = tripService.updateTrip(tripId, tripRequestDTO);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{tripId}")
    public Response deleteTrip(@PathParam("tripId") String tripId) {
        TripSubmissionResult tripSubmissionResult = tripService.deleteTrip(tripId);
        if (tripSubmissionResult == null || !tripSubmissionResult.isSuccess()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Trip not found").build();
        }
        return Response.ok(tripSubmissionResult.getMessage()).build();
    }
}
