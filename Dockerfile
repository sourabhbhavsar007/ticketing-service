FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the target directory to the container
COPY target/ticketing-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]