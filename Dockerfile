# Use official Java 17 runtime
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/railway-backend-1.0.0.jar app.jar

# Copy wallet folder
COPY src/main/resources/wallet wallet

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","app.jar"]