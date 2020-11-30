package KafkaConsumerPackage;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

//import org.apache.kafka.common.serialization.StringDeserializer

public class ConsumerApp {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("group.id", "testgroup");
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        System.out.println("Consumer started!");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        List<String> topics = new LinkedList<String>();
        topics.add("mytopic");
        kafkaConsumer.subscribe(topics);
        int delay = 10;
        try{
            int counter = 0;
            while(true){
                ConsumerRecords<String, String> records = kafkaConsumer.poll(10);
                for (ConsumerRecord<String, String> record: records){
                    System.out.println(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(), record.value()));
                }
                Thread.sleep(delay);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            kafkaConsumer.close();
        }

    }
}
