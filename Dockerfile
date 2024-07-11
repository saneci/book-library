FROM openjdk:17-jdk-alpine

RUN addgroup -S saneci && adduser -S booklibrary -G saneci

USER booklibrary:saneci

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.datasource.url=${DATASOURCE_URL}", "-Dspring.datasource.username=${DATASOURCE_USERNAME}", "-Dspring.datasource.password=${DATASOURCE_PASSWORD}", "-jar", "/app.jar"]