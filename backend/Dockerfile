FROM eclipse-temurin:17.0.11_9-jre-ubi9-minimal

WORKDIR /app

COPY target/dalexchange-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application-prod.properties /app/application.properties

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/app/application.properties"]