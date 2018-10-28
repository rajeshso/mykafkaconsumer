package com.n2.kafkaconsumer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import com.n2.event.bar3.MyEvent;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
// TODO: This implementation is not functional. Use the ConsumerExample
@Configuration
@Profile("local")
@ComponentScan
public class KafkaConsumerConfig {

  @Value("${kafka.bootstrap.servers}")
  private String bootstrapServers;

  @Value("${kafka.schema.registry.url}")
  private String schemaRegistryUrl;

  @Value("${spring.kafka.consumer.client-id}")
  private String clientId;

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, MyEvent> kafkaListenerContainerFactory() {

    Map<String, Object> propertiesMap = new HashMap<>();
    // set default config
    propertiesMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    propertiesMap.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
    propertiesMap.put(GROUP_ID_CONFIG, groupId);
    propertiesMap.put(ENABLE_AUTO_COMMIT_CONFIG, false);
    propertiesMap.put(AUTO_OFFSET_RESET_CONFIG, "latest");
    propertiesMap.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    propertiesMap.put(VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    propertiesMap.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
    propertiesMap.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

    ConcurrentKafkaListenerContainerFactory<String, MyEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(propertiesMap));
    //    factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
    factory.getContainerProperties().setSyncCommits(true);
    return factory;
  }
}
