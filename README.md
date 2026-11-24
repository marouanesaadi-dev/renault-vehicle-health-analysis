# Renault Vehicle Health Analysis

Java 21 / Spring Boot solution for the vehicle telemetry exercise. The repository contains two microservices, persistent telemetry and health reports, scoring rules, alerts, Docker Compose, WireMock, and tests.

## Run

Prerequisites: JDK 21, Maven 3.9+, Docker Compose.

    mvn clean verify
    docker compose up --build

## Services

- telemetry-service: validates and stores incoming telemetry, returns 202 Accepted, and triggers analysis.
- vehicle-analysis-service: calls the recall provider, calculates the health score, and exposes reports and alerts.
- WireMock simulates the external recall API.

The exercise does not define score thresholds. The chosen deductions are explicit assumptions in the detailed README; confirm or adjust them with the interviewer.
