# Étape de construction
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire l'artefact en sautant les tests
RUN mvn clean package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:17-jdk-jammy

# Définir le répertoire de travail
WORKDIR /app

# Copier l'artefact généré depuis l'étape de construction
COPY --from=build /app/target/notification-service-0.0.1-SNAPSHOT.jar notification-service.jar

# Exposer le port utilisé par le service
EXPOSE 8083

# Définir les variables d'environnement nécessaires
ENV KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
    MAIL_USERNAME=your-email@example.com \
    MAIL_PASSWORD=your-email-password \
    SPRING_CLOUD_DISCOVERY_ENABLED=true \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "notification-service.jar"]
