# Stage 1: Build the application using JDK
FROM eclipse-temurin:17-jdk-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven or Gradle files (for dependency management)
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Grant execute permissions to the Maven wrapper
RUN chmod +x ./mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Use a JRE for running the application
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for Spring Boot (not actual expose call, just tells developers what to expose)
EXPOSE 8080

# Run the Spring Boot application with the JRE
ENTRYPOINT ["java", "-jar", "app.jar"]
