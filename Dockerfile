FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

# Etapa 2: Execução
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/spring-study-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]