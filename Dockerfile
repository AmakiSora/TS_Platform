FROM openjdk:8
MAINTAINER AmakiSora
ENTRYPOINT java -jar app.jar
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
EXPOSE 1111