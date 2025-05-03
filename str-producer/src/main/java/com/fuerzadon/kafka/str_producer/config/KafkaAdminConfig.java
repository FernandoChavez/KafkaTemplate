package com.fuerzadon.kafka.str_producer.config;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

/*
    1.-Conexión al clúster: El bean kafkaAdmin configura la conexión al clúster de Kafka utilizando los servidores de arranque.
    2.-Creación de topics: El bean topics asegura que el "topic" str-topic esté disponible con las particiones y réplicas configuradas.
*/

@Configuration
public class KafkaAdminConfig {
    @Autowired
    private KafkaProperties kafkaProperties;

    //Kafka admin es una clase que nos va facilitar lo que es la configuracion y administracion de recursos de java
    //Este bean crea una instancia de KafkaAdmin, que es una clase proporcionada por Spring Kafka
    //para administrar recursos de Kafka, como "topics".
    @Bean
    public KafkaAdmin kafkaAdmin(){
        var configs = new HashMap<String,Object>();
        //BOOTSTRAP_SERVERS_CONFIG es el servidor de arranque. Son una lista de servidores de kafka al cual
        //el cliente se va a conectar
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    //Este bean define los "topics" que se crearán automáticamente en el clúster de Kafka al iniciar la aplicación.
    //Aqui configuramos el servidor de arranque, los topics y las particiones
    //Indicamos que tendremos un topic con 2 particiones y 1 replica por particion
    @Bean
    public KafkaAdmin.NewTopics topics(){
        return new KafkaAdmin.NewTopics(
                TopicBuilder
                        .name("str-topic")
                        .partitions(2)
                        .replicas(1)
                        .build()
        );
    }

}
