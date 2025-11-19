# --- Stage 1: Build ---
# Use a Maven image with a JDK 21 to build your application
FROM maven:3.9.0-eclipse-temurin-21-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the executable JAR file
# -DskipTests is often used to speed up the build in the container environment
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime ---
# Use a minimal JRE 21 image for the final production image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the generated JAR file from the 'build' stage
# The wildcard *.jar ensures we capture the exact artifact name.
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# The command to run the application using the JRE
ENTRYPOINT ["java", "-jar", "app.jar"]
