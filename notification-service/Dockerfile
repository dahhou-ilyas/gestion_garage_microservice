FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY pom.xml .

COPY src ./src

COPY .mvn .mvn
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

COPY target/notification-service-0.0.1-SNAPSHOT.jar notification-service.jar

EXPOSE 8083

ENV KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
    MAIL_USERNAME=your-email@example.com \
    MAIL_PASSWORD=your-email-password \
    SPRING_CLOUD_DISCOVERY_ENABLED=true \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

ENTRYPOINT ["java", "-jar", "/app/notification-service.jar"]
