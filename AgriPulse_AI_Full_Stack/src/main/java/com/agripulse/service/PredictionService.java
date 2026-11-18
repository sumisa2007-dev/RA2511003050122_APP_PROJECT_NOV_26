package com.agripulse.service;

import com.agripulse.model.PredictionRequest;
import com.agripulse.model.PredictionResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PredictionService {
    private final JdbcTemplate jdbc;
    public PredictionService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public PredictionResponse create(PredictionRequest r) {
        validate(r);
        // Demonstration rule only. This is not a trained machine-learning model.
        boolean needed = r.soilMoisture() < 30.0 && r.rainfall() < 5.0;
        String advice = needed
          ? "Soil moisture is low and rainfall is limited. Check the field and consider controlled irrigation."
          : "The demonstration rule does not indicate an immediate irrigation need. Continue monitoring the field.";
        jdbc.update("INSERT INTO predictions (crop_type,soil_moisture,temperature,humidity,rainfall,irrigation_needed,recommendation) VALUES (?,?,?,?,?,?,?)",
          r.cropType().trim(), r.soilMoisture(), r.temperature(), r.humidity(), r.rainfall(), needed ? 1 : 0, advice);
        Long id = jdbc.queryForObject("SELECT last_insert_rowid()", Long.class);
        return find(id);
    }

    public List<PredictionResponse> history() {
        return jdbc.query("SELECT * FROM predictions ORDER BY id DESC LIMIT 100", (rs,n) -> map(
          rs.getLong("id"),rs.getString("crop_type"),rs.getDouble("soil_moisture"),rs.getDouble("temperature"),rs.getDouble("humidity"),rs.getDouble("rainfall"),rs.getInt("irrigation_needed") == 1,rs.getString("recommendation"),rs.getString("created_at")));
    }
    private PredictionResponse find(long id) {
        return jdbc.queryForObject("SELECT * FROM predictions WHERE id=?", (rs,n) -> map(
          rs.getLong("id"),rs.getString("crop_type"),rs.getDouble("soil_moisture"),rs.getDouble("temperature"),rs.getDouble("humidity"),rs.getDouble("rainfall"),rs.getInt("irrigation_needed") == 1,rs.getString("recommendation"),rs.getString("created_at")), id);
    }
    private PredictionResponse map(long id,String crop,double moisture,double temp,double humidity,double rain,boolean needed,String advice,String date) {
        return new PredictionResponse(id,crop,moisture,temp,humidity,rain,needed,needed ? "Irrigation may be needed" : "No immediate irrigation indicated",advice,date);
    }
    private void validate(PredictionRequest r) {
        if (r == null || r.cropType() == null || r.cropType().isBlank()) throw new IllegalArgumentException("Please select a crop type.");
        check(r.soilMoisture(),0,100,"Soil moisture must be between 0 and 100%.");
        check(r.temperature(),-10,60,"Temperature must be between -10 and 60 °C.");
        check(r.humidity(),0,100,"Humidity must be between 0 and 100%.");
        check(r.rainfall(),0,500,"Rainfall must be between 0 and 500 mm.");
    }
    private void check(double v,double min,double max,String message) {
        if (!Double.isFinite(v) || v < min || v > max) throw new IllegalArgumentException(message);
    }
}
