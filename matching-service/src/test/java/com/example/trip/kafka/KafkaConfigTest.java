package com.example.trip.kafka;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConfigTest {

    @BeforeAll
    static void setup() {
        // Ensure static fields are initialized from YAML
        new KafkaConfig();
    }

    @Test
    void testKafkaConfigValuesFromYaml() {
        assertEquals("localhost:9092", KafkaConfig.bootstrapServers);
        assertEquals("trip-scheduled", KafkaConfig.topic);
        assertEquals("trip-matched", KafkaConfig.matchedTopic);
        assertEquals("matching-service-group", KafkaConfig.groupId);
    }
}
