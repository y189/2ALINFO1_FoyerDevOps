# syntax=docker/dockerfile:1

ARG NEXUS_URL
ARG REPOSITORY
ARG GROUP_ID
ARG ARTIFACT_ID
ARG VERSION
ARG NEXUS_USER
ARG NEXUS_PASS

FROM openjdk:17-jdk-slim

WORKDIR /app

# Construire l'URL complète du jar dans Nexus
ENV JAR_URL=${NEXUS_URL}/repository/${REPOSITORY}/$(echo ${GROUP_ID} | tr '.' '/')/${ARTIFACT_ID}/${VERSION}/${ARTIFACT_ID}-${VERSION}.jar

# Installer curl (dans l'image slim, pas toujours présent)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Télécharger le jar depuis Nexus en utilisant authentification basique
RUN curl -u ${NEXUS_USER}:${NEXUS_PASS} -fSL "$JAR_URL" -o app.jar

# Exposer le port de l'application (ex: 8086, adapte selon ton app)
EXPOSE 8086

# Commande de lancement de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]

