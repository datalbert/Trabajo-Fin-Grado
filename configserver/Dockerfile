FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/configserver-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} configserver.jar

ENTRYPOINT ["java","-jar","/configserver.jar"]