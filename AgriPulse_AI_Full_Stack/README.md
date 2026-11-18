# AgriPulse AI – Full-Stack Irrigation Recommendation Prototype

A full-stack Java web application with a responsive HTML/CSS/JavaScript frontend, Spring Boot REST backend, and SQLite database.

## Features
- Crop type, soil moisture, temperature, humidity, and rainfall input
- Backend validation for input values
- Demonstration irrigation recommendation
- Prediction history stored in SQLite
- Responsive dashboard and workflow sections

## Technologies
- Frontend: HTML5, CSS3, JavaScript
- Backend: Java 17 and Spring Boot 3
- API: REST/JSON
- Database: SQLite
- Build tool: Maven

## Requirements
- JDK 17 or later
- Maven 3.6 or later
- Internet connection on the first run so Maven can download dependencies

## Run
Open a terminal in this project folder and execute:

```bash
mvn spring-boot:run
```

Open **http://localhost:8080** in your browser. The SQLite database is created at `data/agripulse.db` when the application starts.

## API endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/health` | Check server status |
| POST | `/api/predictions` | Save field readings and create a recommendation |
| GET | `/api/predictions` | Retrieve the latest 100 saved predictions |

Example POST JSON:

```json
{
  "cropType": "Rice",
  "soilMoisture": 25,
  "temperature": 32,
  "humidity": 60,
  "rainfall": 2
}
```

## Project structure
```text
AgriPulse_AI_Full_Stack/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/agripulse/
    │   ├── AgriPulseApplication.java
    │   ├── config/DatabaseConfig.java
    │   ├── controller/PredictionController.java
    │   ├── model/PredictionRequest.java
    │   ├── model/PredictionResponse.java
    │   └── service/PredictionService.java
    └── resources/
        ├── application.properties
        └── static/
            ├── index.html
            ├── styles.css
            └── app.js
```

## Important model limitation
The backend currently uses a simple demonstration rule: irrigation is suggested when soil moisture is below 30% and rainfall is below 5 mm. This is **not a trained AI/ML model**, and the output is not validated agricultural advice. Temperature, humidity, and crop type are collected and stored but are not used by this simple rule. For a genuine AI-based prediction system, train and evaluate a model using a suitable agricultural dataset and integrate it into the backend. Do not claim model accuracy until measured.

## Future enhancements
- Integrate a trained machine-learning model
- Add live weather API data
- Connect IoT soil-moisture sensors
- Add user authentication and downloadable reports
