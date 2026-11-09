# Etapa 1 — build da aplicação (Maven + JDK 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -B -DskipTests dependency:go-offline || true

COPY src src
RUN mvn -q -B -Dmaven.test.skip=true package

# Etapa 2 — imagem de execução (apenas JRE)
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/btg-orders-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
