# Etapa 1: Build da aplicação
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

# Etapa 2: Execução
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia o arquivo JAR gerado na etapa de build
COPY --from=build /app/target/spring-study-0.0.1-SNAPSHOT.jar app.jar

# Adiciona o script wait-for-it para aguardar o banco de dados estar pronto
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Define o comando de inicialização utilizando o wait-for-it.sh
ENTRYPOINT ["/wait-for-it.sh", "database:3306", "--", "java", "-jar", "app.jar", "--spring.profiles.active=prod"]
