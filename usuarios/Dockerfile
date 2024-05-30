FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/usuarios-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} usuarios.jar
ENTRYPOINT ["java","-jar","/usuarios.jar"]