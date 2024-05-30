FROM openjdk:17-jdk-slim

# Set the working directory

ARG JAR_FILE=target/inventario-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} inventario.jar

ENTRYPOINT ["java","-jar","/inventario.jar"]