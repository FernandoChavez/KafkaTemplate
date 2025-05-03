package com.fuerzadon.kafka.str_producer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
public class StringProducerFactoryConfig {

    @Autowired
    private KafkaProperties kafkaProperties;


    //Estamos configurando aqui las propiedades del producer: El servidor de arranque, el serializador y el valor del serializador
    public ProducerFactory<String, String> producerFactory(){
        var configs = new HashMap<String, Object>();
        //Los servidores de arranque para conectarse al clúster de Kafka.
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        //Serializadores para las claves y valores de los mensajes (en este caso, StringSerializer).
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }


    //KafkaTemplate es una clase de utilidad para enviar mensajes a Kafka.
    //Aqui estamos pasando las configuraciones del producer de arriba
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}
