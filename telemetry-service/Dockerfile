FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY telemetry-service/pom.xml telemetry-service/pom.xml
COPY vehicle-analysis-service/pom.xml vehicle-analysis-service/pom.xml
RUN mvn -q -pl telemetry-service -am dependency:go-offline
COPY telemetry-service/src telemetry-service/src
RUN mvn -q -pl telemetry-service -am package -DskipTests
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/telemetry-service/target/telemetry-service-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
