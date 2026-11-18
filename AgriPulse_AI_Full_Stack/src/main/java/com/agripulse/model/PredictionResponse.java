package com.agripulse.model;
public record PredictionResponse(long id, String cropType, double soilMoisture, double temperature, double humidity, double rainfall, boolean irrigationNeeded, String status, String recommendation, String createdAt) {}
