package com.example.trip.kafka;

// ...existing imports...
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.YamlInjector;
import org.example.YamlValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @YamlValue(key = "kafka.bootstrapServers")
    public static String bootstrapServers;

    @YamlValue(key = "kafka.matchedTopic")
    public static String matchedTopic;

    @YamlValue(key = "kafka.groupId")
    public static String groupId;

    static {
        // Load properties from YAML file
        YamlInjector.inject(KafkaConfig.class);
        logger.info("KafkaConfig initialized with bootstrapServers: {}, matchedTopic: {}, groupId: {}",
                bootstrapServers, matchedTopic, groupId);
    }

    public static Properties consumerProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}
