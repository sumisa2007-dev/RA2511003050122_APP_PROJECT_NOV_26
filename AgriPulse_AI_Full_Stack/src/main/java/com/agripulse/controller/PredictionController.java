package com.agripulse.controller;
import com.agripulse.model.PredictionRequest;
import com.agripulse.model.PredictionResponse;
import com.agripulse.service.PredictionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class PredictionController {
    private final PredictionService service;
    public PredictionController(PredictionService service) { this.service = service; }
    @GetMapping("/health") public Map<String,String> health() { return Map.of("status","running","application","AgriPulse AI"); }
    @PostMapping("/predictions") @ResponseStatus(HttpStatus.CREATED)
    public PredictionResponse predict(@RequestBody PredictionRequest request) { return service.create(request); }
    @GetMapping("/predictions") public List<PredictionResponse> history() { return service.history(); }
    @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> badRequest(IllegalArgumentException ex) { return Map.of("error",ex.getMessage()); }
}
