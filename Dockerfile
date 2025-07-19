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

# Installer curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Télécharger le jar depuis Nexus
RUN set -ex && \
    GROUP_PATH=$(echo "$GROUP_ID" | tr '.' '/') && \
    JAR_URL="${NEXUS_URL}/repository/${REPOSITORY}/${GROUP_PATH}/${ARTIFACT_ID}/${VERSION}/${ARTIFACT_ID}-${VERSION}.jar" && \
    curl -u ${NEXUS_USER}:${NEXUS_PASS} -fSL "$JAR_URL" -o app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]
