FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/gatewayserver-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} gatewayserver.jar

ENTRYPOINT ["java","-jar","/gatewayserver.jar"]