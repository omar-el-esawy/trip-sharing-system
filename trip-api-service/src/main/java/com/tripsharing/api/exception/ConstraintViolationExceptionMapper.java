package com.tripsharing.api.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");

        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }
}
