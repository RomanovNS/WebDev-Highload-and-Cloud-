package KafkaProducerPackage;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerApp {
    public static void main(String[] args){

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        System.out.println("Producer started!");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        int delay = 20;
        double multiplier;

        try{
            int counter = 0;
            while(true){
                kafkaProducer.send(new ProducerRecord<String, String>("mytopic", Integer.toString(counter), "test message - " + counter));
                counter++;
                multiplier = (1.0 + Math.sin(Math.PI * (System.currentTimeMillis() % 3000 / 1500.0))) / 2.0;
                Thread.sleep((long)(delay * multiplier));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            kafkaProducer.close();
        }
    }
}
