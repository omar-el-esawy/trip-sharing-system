package com.example.trip.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.YamlInjector;
import org.example.YamlValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @YamlValue(key = "kafka.bootstrapServers")
    public static String bootstrapServers;

    @YamlValue(key = "kafka.topic")
    public static String topic;

    @YamlValue(key = "kafka.matchedTopic")
    public static String matchedTopic;

    @YamlValue(key = "kafka.groupId")
    public static String groupId;

    static {
        // Load properties from YAML file
        YamlInjector.inject(KafkaConfig.class);
        logger.info("KafkaConfig initialized with bootstrapServers: {}, topic: {}, matchedTopic: {}, groupId: {}",
                bootstrapServers, topic, matchedTopic, groupId);
    }

    public static Properties producerProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

    public static Properties consumerProps() {
        logger.info("KafkaConfig.consumerProps() called with bootstrapServers: {}, groupId: {}", bootstrapServers, groupId);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}
