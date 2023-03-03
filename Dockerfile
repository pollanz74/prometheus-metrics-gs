FROM maven:3.8.6-eclipse-temurin-11-alpine AS build-env
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src src
RUN mvn -B package

#FROM gcr.io/distroless/java11-debian11
FROM bellsoft/liberica-openjdk-alpine:11
COPY --from=build-env target/prometheus-metrics-gs*.jar /app/prometheus-metrics-gs.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "prometheus-metrics-gs.jar"]