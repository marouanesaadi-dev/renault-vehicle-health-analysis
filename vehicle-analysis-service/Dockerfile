FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY telemetry-service/pom.xml telemetry-service/pom.xml
COPY vehicle-analysis-service/pom.xml vehicle-analysis-service/pom.xml
RUN mvn -q -pl vehicle-analysis-service -am dependency:go-offline
COPY vehicle-analysis-service/src vehicle-analysis-service/src
RUN mvn -q -pl vehicle-analysis-service -am package -DskipTests
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/vehicle-analysis-service/target/vehicle-analysis-service-1.0.0.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
