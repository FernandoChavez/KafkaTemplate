package com.fuerzadon.kafka.str_producer.resources;

import com.fuerzadon.kafka.str_producer.services.StringProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class StringProducerResource {

    //Utiliza el servicio StringProducerService para enviar mensajes al "topic" de Kafka
    @Autowired
    private StringProducerService stringProducerService;


    //Maneja solicitudes HTTP POST en la ruta /producer.
    //Recibe un mensaje en el cuerpo de la solicitud (@RequestBody String message).
    //Llama al método sendMessage del servicio para enviar el mensaje a Kafka.
    //Devuelve una respuesta HTTP con el estado 201 CREATED si el mensaje se envió correctamente.
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody String message){
        stringProducerService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
