package com.airtribe.library.controller;

import com.airtribe.library.domain.Book;
import com.airtribe.library.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{patronId}")
    public ResponseEntity<List<Book>> getRecommendations(@PathVariable String patronId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(patronId));
    }
}
