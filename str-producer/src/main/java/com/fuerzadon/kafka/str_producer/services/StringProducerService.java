package com.fuerzadon.kafka.str_producer.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class StringProducerService {

    @Autowired
    private KafkaTemplate<String, String> KafkaTemplate;

    public void sendMessage(String message){
        KafkaTemplate.send("str-topic", message). whenComplete((result, ex) -> {
            //En dado caso de que haya un error
            if(ex != null){
                log.error("Error, al enviar el mensaje: {}", ex.getMessage());
            }

            log.info("Message enviado con exito: {}", result.getProducerRecord().value());
            log.info("Particion {}, Offset {}", result.getRecordMetadata().partition(), result.getRecordMetadata().offset());



        });
    }


}
