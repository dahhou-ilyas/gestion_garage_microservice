FROM maven:3.8.7-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/cars-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENV DB_HOST=localhost \
    DB_PORT=5432 \
    DB_NAME=cars_db \
    DB_USERNAME=nom_utilisateur \
    DB_PASSWORD=mot_de_passe \
    JPA_DDL_AUTO=create \
    SHOW_SQL=true \
    KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
    CUSTOMER_SERVICE_URL=http://localhost:8081 \
    SPRING_CLOUD_DISCOVERY_ENABLED=true \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

ENTRYPOINT ["java", "-jar", "app.jar"]
