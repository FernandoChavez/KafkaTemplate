IMPORTANTE: Utiliza el IDE IntelliJ y descarga el plugin de lombok


//PRODUCER

Este programa es una aplicación Spring Boot que utiliza Apache Kafka para enviar mensajes a 
un "topic" (un canal de comunicación en Kafka). Aquí te explico las partes principales:

	1.- Configuración de Kafka (KafkaAdminConfig):
-Configura un cliente administrador de Kafka (KafkaAdmin) para gestionar recursos de Kafka.
-Define un "topic" llamado str-topic con 2 particiones y 1 réplica.

	2.- Servicio de Producción de Mensajes (StringProducerService):
-Usa un KafkaTemplate para enviar mensajes al "topic" str-topic.
-Registra logs para confirmar si el mensaje fue enviado correctamente o si hubo errores.

	3.- Controlador REST (StringProducerResource):
-Expone un endpoint HTTP (/producer) que permite enviar mensajes a Kafka mediante una solicitud POST con el mensaje en el cuerpo.

	4.- Clase Principal (StrProducerApplication):
-Inicia la aplicación Spring Boot.

Flujo de trabajo:
1.- El usuario envía un mensaje al endpoint /producer mediante una solicitud POST.
2.- El controlador (StringProducerResource) recibe el mensaje y lo pasa al servicio (StringProducerService).
3.- El servicio envía el mensaje al "topic" str-topic en Kafka.
4.- Kafka distribuye el mensaje a las particiones del "topic".


CONSUMER

El consumer va a enviar el mensaje a un topic y nosotros lo recibimos en ese topic

Configuración del servidor y Kafka:

1.- En el archivo application.yml, se configuran el puerto del servidor (8100 por defecto) y el servidor de Kafka (localhost:29092 por defecto). Estas configuraciones pueden ser sobrescritas mediante variables de entorno (PORT y KAFKA_HOST).
Dockerfile:

2.- El Dockerfile utiliza una imagen base de OpenJDK 21 para ejecutar la aplicación. Copia el archivo JAR generado por Gradle (build/libs/*.jar) al contenedor y lo ejecuta con java -jar.
KafkaListener:

3.- La aplicación  tiene un métodos con @KafkaListener que escucha mensajes de un tópico de Kafka. Este método procesa los mensajes en tiempo real.
Gradle:

4.- El proyecto usa Gradle para gestionar dependencias y construir la aplicación. Se asegura de incluir las dependencias necesarias para Spring Boot y Kafka.
Flujo general:

--La aplicación se inicia en el puerto configurado.
--Se conecta al servidor Kafka especificado.
--Escucha mensajes de un tópico Kafka y los procesa según la lógica implementada.

Interceptor: Es una caracteristica que nos va a permitir modificar los registros. Todo lo que envia un productor y antes de que llegue al consumidor, kafka, a traves de estos interceptores, 
aplicara una serie de pasos personalizados para realizar una accion, como un login, una validacion, transformacion, etc; antes de que los registros sean procesados completamente por el consumidor


CONVERTIR NUESTROS CONSUMER Y PRODUCER EN IMAGENES CORRERLO EN DOCKER DESKTOP Y SUBIRLO A DOCKER HUB

1.-Crear un dockerfile tanto para el consumer como para el producer
2.- Agregar la configuraciones como:

 FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

3.- Crear el jar con los respectivos metodos, de acuerdo al  laherramienta de automatización de builds que estemos usando

maven
1.- clean
2.- package


gradle
1.- clean
2.- build

NOTA: CUALQUIER CAMBIO QUE HAGAS AL CODIGO DEL PROYECTO, ES NECESARIO VOLVER A GENERAR EL JAR CON EL CODIGO ACTUALIZADO. TAMBIEN SERA ENCESARIO ELIMINAR LAS IMAGENES VIEJAS QUE SE REMPLAZARAN EN DOCKER-DESKTOP

Para subir una imagen a dockerhub 
1.- Necesitamos logearnos a nuestra cuenta en cmd con:
   >docker login

2.- Creamos la imagen
Para eso en cmd nos colocamos en la raiz del producer y ejecutamos lo siguiente para crear la imagen del producere n base su dockerfile
>docker build -t usuarioDocker/proyectoProducer:1.0.0

ejemplo:
>docker build -t thefuerzadon/str-producer:1.0.0 .


Lo mismo para el consumer. Nos colocamos en la raiz del consumer y ejecutamos
>docker build -t thefuerzadon/str-consumer:1.0.0 .

3.- Despues de haber generado las imagenes tanto del consumer como del producer, es tiempo de agregar los servicios en docker compose y correr nuevos contenedores  en docker-desktop

str-producer:
    image: thefuerzadon/str-producer:1.0.0
    networks:
      - broker-kafka
    depends_on:
      - kafka
    ports:
      - "8000:8000"
    environment:
      KAFKA_HOST: kafka:29092

  str-consumer:
    image: thefuerzadon/str-consumer:1.0.0
    networks:
      - broker-kafka
    depends_on:
      - kafka
    ports:
      - "8100:8100"
    environment:
      KAFKA_HOST: kafka:29092

4.- Ejecutas para correr los contenedores
> docker-compose up -d
NOTA: ASEGURATE DE QUE LA VERSION DE JAVA ESPECIFICADA EN DOCKERFILE SEA LA MISMA VERSION DE JAVA CON LA QUE SE TRABAJO LA APP


5.- Subir las imagenes a nuestro docker hub
-Login a nuestra cuenta
-Escribir:

>docker push thefuerzadon/str-producer:1.0.0
>docker push thefuerzadon/str-consumer:1.0.0
