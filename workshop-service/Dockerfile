# Étape de construction
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source de l'application
COPY src ./src

# Construire l'artefact de l'application
RUN mvn clean package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:17-jdk-jammy

# Définir le répertoire de travail
WORKDIR /app

# Copier l'artefact depuis l'étape précédente
COPY --from=build /app/target/workshop-service-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port utilisé par le service
EXPOSE 8084

# Définir les variables d'environnement par défaut
ENV SERVER_PORT=8084 \
    APPLICATION_NAME=workshop-service \
    DB_HOST=localhost \
    DB_PORT=5432 \
    DB_NAME=workshop_db \
    DB_USERNAME=nom_utilisateur \
    DB_PASSWORD=mot_de_passe \
    JPA_DDL_AUTO=create \
    SHOW_SQL=true \
    KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
    EUREKA_DEFAULT_ZONE=http://localhost:8761/eureka/ \
    LOG_LEVEL=DEBUG

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
