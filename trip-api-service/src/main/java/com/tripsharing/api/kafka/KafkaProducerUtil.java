package com.tripsharing.api.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerUtil {
    private static final KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.producerProps());

    public static void sendTripScheduledEvent(String tripId) {
        ProducerRecord<String, String> record = new ProducerRecord<>(KafkaConfig.topic, tripId, tripId);
        producer.send(record);
    }

    public static void close() {
        producer.close();
    }
}
