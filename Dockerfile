# Use AdoptOpenJDK as the base image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/currency-converter-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your Spring Boot app will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
