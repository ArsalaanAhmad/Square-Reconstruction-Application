# Use an official Gradle image from the Docker Hub
FROM gradle:8.8-jdk17 AS builder

# Set the working directory
WORKDIR /app

# Copy the gradle wrapper and gradle scripts
COPY gradlew .
COPY gradle/ gradle/

# Copy the rest of the project files
COPY . .

# Build the application
RUN ./gradlew build --no-daemon

# Use a smaller base image for the final stage
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built application from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]