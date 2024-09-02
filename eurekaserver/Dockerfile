FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/eurekaserver-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} eurekaserver.jar

ENTRYPOINT ["java","-jar","/eurekaserver.jar"]