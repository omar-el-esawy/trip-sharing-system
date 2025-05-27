# Stage 1: Build shadow JAR
FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /project
COPY ../ .
RUN gradle :trip-api-service:shadowJar --no-daemon --stacktrace

# Stage 2: Slim runtime
FROM openjdk:17
WORKDIR /app

# Replace the name if different
COPY --from=builder /project/trip-api-service/build/libs/trip-api-service-1.0-SNAPSHOT-all.jar app.jar

CMD ["java", "-jar", "app.jar"]
