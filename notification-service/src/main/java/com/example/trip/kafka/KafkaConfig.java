package com.example.trip.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.YamlInjector;
import org.example.YamlValue;

import java.util.Properties;

public class KafkaConfig {

    @YamlValue(key = "kafka.bootstrapServers")
    public static String bootstrapServers;

    @YamlValue(key = "kafka.matchedTopic")
    public static String matchedTopic;

    @YamlValue(key = "kafka.groupId")
    public static String groupId;

    static {
        // Load properties from YAML file
        YamlInjector.inject(KafkaConfig.class);
        System.out.println("KafkaConfig initialized with bootstrapServers: " + bootstrapServers + ", matchedTopic: " + matchedTopic + ", groupId: " + groupId);
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
