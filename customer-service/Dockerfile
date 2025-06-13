# Étape de construction
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier de configuration Maven
COPY pom.xml .

# Télécharger les dépendances nécessaires pour une construction hors-ligne
RUN mvn dependency:go-offline -B

# Copier tout le projet
COPY src ./src

# Construire l'application en sautant les tests
RUN mvn clean package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:17-jdk-jammy

# Définir le répertoire de travail
WORKDIR /app

# Copier l'artefact généré à partir de l'étape précédente
COPY --from=build /app/target/customer-service-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port utilisé par le service
EXPOSE 8081

# Définir les variables d'environnement pour la configuration
ENV DB_HOST=localhost \
    DB_PORT=5432 \
    DB_NAME=customer_db \
    DB_USERNAME=nom_utilisateur \
    DB_PASSWORD=mot_de_passe \
    JPA_DDL_AUTO=create \
    SHOW_SQL=true \
    KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
    SPRING_CLOUD_DISCOVERY_ENABLED=true \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
