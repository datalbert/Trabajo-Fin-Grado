FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/reservas-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} reservas.jar

ENTRYPOINT ["java","-jar","/reservas.jar"]