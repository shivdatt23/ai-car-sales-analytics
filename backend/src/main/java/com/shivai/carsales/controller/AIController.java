package com.shivai.carsales.controller;

import com.shivai.carsales.services.AIQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIQueryService aiQueryService;

    public AIController(AIQueryService aiQueryService) {
        this.aiQueryService = aiQueryService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question){
        return ResponseEntity.ok(aiQueryService.process(question));
    }





}
