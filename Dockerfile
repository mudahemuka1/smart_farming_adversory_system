# STAGE 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY backend/pom.xml .
COPY backend/src ./src

# Build the application package
RUN mvn clean package -DskipTests

# STAGE 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set Environment Variables for Production defaults
ENV SERVER_PORT=8080

# Expose the port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]
