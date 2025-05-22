package com.tripsharing.api.resource;

//import com.tripsharing.api.dto.TripDTO;
import com.client.generated.TripDTO;
import com.tripsharing.api.service.MatchingSoapClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/trips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TripResource {

    private static final Map<String, TripDTO> tripStore = new ConcurrentHashMap<>();
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
        if (trip == null || trip.getTripId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Trip ID required").build();
        }
        tripStore.put(trip.getTripId(), trip);
        matchingSoapClient.submitTrip(trip);

        return Response.status(Response.Status.CREATED).entity(trip).build();
    }

    @GET
    @Path("/{tripId}")
    public Response getTrip(@PathParam("tripId") String tripId) {
        TripDTO trip = tripStore.get(tripId);
        if (trip == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Trip not found").build();
        }
        matchingSoapClient.getTrip(tripId);
        return Response.ok(trip).build();
    }

    @PUT
    @Path("/{tripId}")
    public Response updateTrip(@PathParam("tripId") String tripId, TripDTO trip) {
        if (!tripStore.containsKey(tripId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("Trip not found").build();
        }
        trip.setTripId(tripId);
        tripStore.put(tripId, trip);

        matchingSoapClient.updateTrip(trip);
        return Response.ok(trip).build();
    }

    @DELETE
    @Path("/{tripId}")
    public Response deleteTrip(@PathParam("tripId") String tripId) {
        TripDTO removed = tripStore.remove(tripId);
        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Trip not found").build();
        }

        matchingSoapClient.deleteTrip(tripId);
        return Response.noContent().build();
    }
}
