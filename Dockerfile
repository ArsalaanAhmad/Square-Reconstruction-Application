# Use a base image with Java and Gradle installed
FROM gradle:8.8-jdk17 AS builder

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and the build files
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Copy the source code
COPY src ./src

# Ensure Gradle wrapper has executable permissions
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build --no-daemon

# Use a smaller base image for the final stage
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built application from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]