package com.fuerzadon.kafka.listeners;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class StrConsumerListener {

    //El consumer va a enviar el mensaje a un topic y nosotros lo recibimos en ese topic
    //Para poder estar escuchadno, usamos KafkaListener
    //groupId es un id que identificara a un grupo de consumidores

    //Aqui el listener1 escuchara la particion 0 del topic "str-topic" en el group-1
    //El listener 1 pertenece al group-1
    @KafkaListener(groupId= "group-1",
            topicPartitions = @TopicPartition(topic = "str-topic", partitions={"0"}),
            containerFactory = "validMessageContainerFactory")
    public void Listener1(String message){
        log.info("LISTENER1 ::: Recibiendo un mensaje {}", message);
    }

    @KafkaListener(groupId= "group-1",
            topicPartitions = @TopicPartition(topic = "str-topic", partitions={"1"}),
            containerFactory = "validMessageContainerFactory")
    public void Listener2(String message){
        log.info("LISTENER2 ::: Recibiendo un mensaje {}", message);
    }

    //Cuando configuramos un listener de la sig manera, sin el @topicPartitions, quiere decir que
    //escucha todas las particiones del group-2
    @KafkaListener(groupId= "group-2", topics = "str-topic", containerFactory = "validMessageContainerFactory")
    public void Listener3(String message){
        log.info("LISTENER3 ::: Recibiendo un mensaje {}", message);
    }




}
