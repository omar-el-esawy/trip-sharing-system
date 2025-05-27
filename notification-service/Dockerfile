FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /project
COPY ../ .
RUN gradle :notification-service:shadowJar --no-daemon

FROM openjdk:17
WORKDIR /app
COPY --from=builder /project/notification-service/build/libs/*-all.jar app.jar
CMD ["java", "-jar", "app.jar"]
