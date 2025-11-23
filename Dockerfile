# 🛠️ Build Stage
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 🚀 Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Whtsapp-Message-POC-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/Downloads

ENV DOWNLOAD_DIR=/app/Downloads
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
