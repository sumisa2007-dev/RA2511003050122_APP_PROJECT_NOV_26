package com.agripulse.model;
public record PredictionRequest(String cropType, double soilMoisture, double temperature, double humidity, double rainfall) {}
