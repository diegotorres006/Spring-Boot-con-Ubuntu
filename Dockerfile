# ETAPA 1: BUILD (Compilación)
FROM gradle:8.5-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN ./gradlew build -x test --no-daemon

# ETAPA 2: RUNTIME (Ejecución ligera)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el JAR generado en la etapa anterior
COPY --from=builder /build/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
