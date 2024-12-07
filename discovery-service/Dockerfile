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
COPY --from=build /app/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port utilisé par le service
EXPOSE 8761

# Définir les variables d'environnement nécessaires
ENV SPRING_APPLICATION_NAME=discovery-service \
    SERVER_PORT=8761 \
    EUREKA_CLIENT_FETCH_REGISTRY=false \
    EUREKA_CLIENT_REGISTER_WITH_EUREKA=false

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
