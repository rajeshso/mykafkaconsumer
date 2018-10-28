package com.n2.kafkaconsumer;

import com.n2.event.bar3.MyEvent;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// TODO: This implementation is not functional. Use the ConsumerExample
@Component
public class MyKafkaConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaConsumer.class);

  private CountDownLatch latch = new CountDownLatch(1);
  @Autowired private ApplicationContext context;

  public CountDownLatch getLatch() {
    return latch;
  }

  @PostConstruct
  public void test() {
    System.out.println(
        "Bean names: " + Arrays.toString(context.getBeanNamesForType(MyKafkaConsumer.class)));
  }

  // TODO: The problem is the containerFactory is initialized afgter the Kafka Listener.
  // Hence, the properties including the Deserializer is not recognized.
  // Solution: Get the containerFactory initialized and then start the kafka listener

  @KafkaListener(topics = "${kafka.topic.boot}", containerFactory = "kafkaListenerContainerFactory")
  public void onMessage(final MyEvent customerEvent) {
    System.out.println(customerEvent);
  }
  //  public void receive(ConsumerRecord<String, MyEvent> consumerRecord) {
  ////    MyEvent myevent = consumerRecord.value();
  //    LOGGER.info("received payload= ", consumerRecord.toString());
  //    latch.countDown();
  //  }
}
