package com.agripulse.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import java.nio.file.Files;
import java.nio.file.Path;
@Configuration
public class DatabaseConfig {
    @Bean CommandLineRunner initializeDatabase(JdbcTemplate jdbc) {
        return args -> {
            Files.createDirectories(Path.of("data"));
            jdbc.execute("CREATE TABLE IF NOT EXISTS predictions (id INTEGER PRIMARY KEY AUTOINCREMENT, crop_type TEXT NOT NULL, soil_moisture REAL NOT NULL, temperature REAL NOT NULL, humidity REAL NOT NULL, rainfall REAL NOT NULL, irrigation_needed INTEGER NOT NULL, recommendation TEXT NOT NULL, created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP)");
        };
    }
}
