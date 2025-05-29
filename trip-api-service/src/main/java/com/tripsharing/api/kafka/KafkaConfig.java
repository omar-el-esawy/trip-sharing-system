package com.tripsharing.api.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.YamlInjector;
import org.example.YamlValue;

import java.util.Properties;

public class KafkaConfig {

    @YamlValue(key = "kafka.bootstrapServers")
    public static String bootstrapServers;

    @YamlValue(key = "kafka.topic")
    public static String topic;


    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KafkaConfig.class);

    static {
        YamlInjector.inject(KafkaConfig.class);
        logger.info("KafkaConfig initialized with bootstrapServers: {}, topic: {}", bootstrapServers, topic);
    }

    public static Properties producerProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }
}
