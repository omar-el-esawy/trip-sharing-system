package com.tripsharing.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.validation.ValidationFeature;

@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("com.tripsharing.api");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        register(new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        register(ValidationFeature.class);

    }
}
