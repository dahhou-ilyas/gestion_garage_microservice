# Utilise une image Maven compatible avec Spring Boot 3.3.5 et Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie uniquement le pom.xml en premier pour profiter du cache des dépendances Docker
COPY pom.xml .

# Télécharge les dépendances du projet sans compiler
RUN mvn dependency:go-offline -B

# Copie le reste des sources du projet
COPY src ./src

# Compile le projet sans exécuter les tests
RUN mvn clean package -DskipTests

# Utilise une image Java runtime légère
FROM eclipse-temurin:17-jre-jammy

# Définit le répertoire de travail
WORKDIR /app

# Copie le jar compilé de l'étape de build
COPY --from=build /app/target/billing-service-0.0.1-SNAPSHOT.jar app.jar

# Expose le port sur lequel l'application va tourner
EXPOSE 8085

# Variables d'environnement pour la configuration
ENV KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
    DB_HOST=localhost \
    DB_PORT=5432 \
    DB_NAME=billing_service \
    DB_USERNAME=nom_utilisateur \
    DB_PASSWORD=mot_de_passe \
    JPA_DDL_AUTO=create \
    SHOW_SQL=true \
    SPRING_CLOUD_DISCOVERY_ENABLED=true \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

# Point d'entrée pour démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]