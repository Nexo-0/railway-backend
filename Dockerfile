# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

# Install Maven
RUN apt-get update -q && apt-get install -y -q maven

# Copy pom first to cache dependency downloads separately
COPY pom.xml .
RUN mvn dependency:go-offline -q 2>/dev/null || true

# Copy source and build the JAR (skip tests for faster builds)
COPY src/ src/
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /build/target/railway-backend-1.0.0.jar app.jar

# Copy Oracle Wallet (needed for DB connection)
COPY src/main/resources/wallet wallet

# Expose the port Render routes traffic to
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]