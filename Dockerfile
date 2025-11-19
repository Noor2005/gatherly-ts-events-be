# --- Stage 1: Build ---
# Using the stable 'maven:3-jdk-21-slim' image
FROM maven:3.9.7-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Build the executable JAR file
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime ---
# Using the minimal 'eclipse-temurin:21-jre-alpine' image
FROM openjdk:21-jre-alpine

WORKDIR /app

# Copy the generated JAR file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
