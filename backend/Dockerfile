## =======================================================
 ## STAGE 1: Build Phase
 ## =======================================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY ./backend/pom.xml ./pom.xml
COPY ./backend/src ./src
RUN mvn clean package -DskipTests
 #
 ## =======================================================
 ## STAGE 2: Combined Runtime Phase (Java + MySQL)
 ## =======================================================
FROM eclipse-temurin:21-jre-noble
 #
 ## Install MySQL Server natively inside the container
RUN apt-get update && apt-get install -y mysql-server && rm -rf /var/lib/apt/lists/*
 #
 WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
 #
# Create a startup script to initialize MySQL and run Java using a multi-line heredoc
RUN cat <<'EOF' > /app/entrypoint.sh
# !/bin/sh

# Start the MySQL system service in the background
service mysql start

# Wait for MySQL to initialize its sockets properly
until mysqladmin ping --silent; do
    echo "Waiting for local MySQL server to start..."
    sleep 2
done

# Initialize the database and user access controls
mysql -e "CREATE DATABASE IF NOT EXISTS nexus_gaming;"
mysql -e "CREATE USER IF NOT EXISTS 'avnadmin'@'localhost' IDENTIFIED BY 'Sharingan1!';"
mysql -e "GRANT ALL PRIVILEGES ON nexus_gaming.* TO 'avnadmin'@'localhost';"
mysql -e "FLUSH PRIVILEGES;"

echo "Local MySQL database configured successfully. Starting Spring Boot API..."

# Start the Spring Boot API application process
exec java -Dspring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration -jar app.jar
EOF

# FIX: Automatically convert Windows CRLF line endings to Linux LF line endings
RUN sed -i 's/\r$//' /app/entrypoint.sh

# Grant execution permissions to your startup script
RUN chmod +x /app/entrypoint.sh
 #
# Expose port 8080 for Render routing metadata
EXPOSE 8080
 #
ENTRYPOINT ["/app/entrypoint.sh"]