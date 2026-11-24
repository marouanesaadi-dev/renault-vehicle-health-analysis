# Vehicle Health Analysis Platform

Solution de l’exercice Renault : réception des données télématiques, analyse asynchrone, calcul d’un score de santé et consultation des alertes.

## Architecture

- **telemetry-service** : valide et persiste chaque télémétrie, répond `202 Accepted`, puis déclenche l’analyse en arrière-plan.
- **vehicle-analysis-service** : applique les règles métier, consulte le provider externe de rappels et conserve le dernier rapport par VIN.
- **WireMock** : simule le provider externe en local et dans les tests d’intégration.

Le flux entre services est HTTP pour garder le périmètre exécutable sans broker. L’analyse démarre après l’accusé de réception ; le rapport peut donc ne pas être disponible immédiatement.

## Hypothèses métier à confirmer

L’énoncé ne donne pas les seuils ni les pénalités. Les règles initiales sont centralisées dans `HealthScoreCalculator` et documentées : kilométrage ≥ 50 000/100 000/150 000 km (-5/-10/-15), pression hors plage 2,0–2,8 bar (-5 par pneu, -10 si <1,8), batterie <12,2/<11,8 V (-5/-15), température >100/>110 °C (-10/-20), carburant <10 % (-5), codes défaut (-5 chacun, maximum -20), rappels actifs (-10 chacun, maximum -20). Le score est borné entre 0 et 100.

Ces valeurs sont des hypothèses de démonstration, pas des spécifications Renault. Le provider externe expose `GET /api/recalls/{vin}` et renvoie `{ "activeRecall": true }`.

## Démarrage

Prérequis : JDK 21, Maven 3.9+ et Docker Compose.

```bash
mvn clean verify
docker compose up --build
```

Endpoints :

- `POST http://localhost:8081/api/v1/telemetry`
- `GET http://localhost:8082/api/v1/vehicles/{vin}/health-report`
- `GET http://localhost:8082/api/v1/vehicles/{vin}/alerts`

Exemple de télémétrie :

```json
{
  "vin": "VF1AAAAA123456789",
  "timestamp": "2026-11-24T10:00:00Z",
  "mileageKm": 84500,
  "tirePressuresBar": [2.3, 2.2, 1.9, 2.4],
  "batteryVoltage": 12.1,
  "engineTemperatureC": 96,
  "fuelLevelPercent": 35,
  "errorCodes": ["P0420"]
}
```

## Tests

Les tests unitaires couvrent les pénalités et les bornes du score. Les tests web vérifient l’acceptation/rejet d’une télémétrie. Le test d’intégration vérifie le calcul, l’appel au provider simulé par WireMock et la lecture du rapport.
