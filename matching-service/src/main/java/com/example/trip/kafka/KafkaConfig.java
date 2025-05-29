package com.example.trip.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.YamlInjector;
import org.example.YamlValue;

import java.util.Properties;

public class KafkaConfig {

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
        System.out.println("KafkaConfig initialized with bootstrapServers: " + bootstrapServers + ", topic: " + topic + ", matchedTopic: " + matchedTopic + ", groupId: " + groupId);
    }

    public static Properties producerProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

    public static Properties consumerProps() {
        System.out.println("KafkaConfig.consumerProps() called with bootstrapServers: " + bootstrapServers + ", groupId: " + groupId);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}
