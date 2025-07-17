# Étape 1 : Build avec Maven + JDK
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

ARG SKIP_TESTS=false
RUN if [ "$SKIP_TESTS" = "true" ]; then mvn clean package -DskipTests; else mvn clean package; fi

# Étape 2 : Image runtime légère
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
