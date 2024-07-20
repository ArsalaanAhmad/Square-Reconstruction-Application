# Use the official Gradle image to create a build artifact
FROM gradle:7.3.3-jdk11 as builder
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build --no-daemon

# Use the official OpenJDK image to run the application
FROM openjdk:11-jre-slim
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

