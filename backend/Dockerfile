# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code directly into /app
COPY backend/pom.xml .
COPY backend/src ./src

# Package the application skipping unit tests
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app

# FIXED: Changed /app/backend/target/ to /app/target/
COPY --from=build /app/target/*.jar app.jar

# Inform Docker/Render that the app listens on port 8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]