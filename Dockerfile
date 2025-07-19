# syntax=docker/dockerfile:1

# Déclaration initiale des ARGs (utile pour FROM si besoin dynamique)
ARG NEXUS_URL
ARG REPOSITORY
ARG GROUP_ID
ARG ARTIFACT_ID
ARG VERSION
ARG NEXUS_USER
ARG NEXUS_PASS

FROM openjdk:17-jdk-slim

# ⚠️ Redéclarer ici les ARGs pour les rendre accessibles dans la suite
ARG NEXUS_URL
ARG REPOSITORY
ARG GROUP_ID
ARG ARTIFACT_ID
ARG VERSION
ARG NEXUS_USER
ARG NEXUS_PASS

WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Télécharger le jar depuis Nexus
RUN set -ex && \
    echo "GROUP_ID=${GROUP_ID}" && \
    echo "ARTIFACT_ID=${ARTIFACT_ID}" && \
    echo "VERSION=${VERSION}" && \
    echo "NEXUS_URL=${NEXUS_URL}" && \
    echo "REPOSITORY=${REPOSITORY}" && \
    GROUP_PATH=$(echo "$GROUP_ID" | tr '.' '/') && \
    JAR_URL="${NEXUS_URL}/repository/${REPOSITORY}/${GROUP_PATH}/${ARTIFACT_ID}/${VERSION}/${ARTIFACT_ID}-${VERSION}.jar" && \
    echo "Downloading: $JAR_URL" && \
    curl -u "${NEXUS_USER}:${NEXUS_PASS}" -fSL "$JAR_URL" -o app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]
