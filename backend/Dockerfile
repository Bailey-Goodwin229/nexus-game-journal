# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code directly to avoid copying unwanted local files
COPY pom.xml .
COPY src ./src

# Package the application skipping unit tests for deployment speed
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the compiled JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Inform Docker/Render that the app listens on port 8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]