package com.example.vector_search_mistral.controller;

import com.example.vector_search_mistral.service.JsonVectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2/docs")
@RequiredArgsConstructor
public class JsonVectorController {
    private final JsonVectorService jsonVectorService;

    @GetMapping("")
    public List<Map<String, Object>> getAllDocuments() {
        return jsonVectorService.getAllProcessedDocuments();
    }

    @GetMapping("/create")
    public ResponseEntity<String> addDocuments() {
        try {
            jsonVectorService.processAndAddDocuments();
            return ResponseEntity.ok("Documents processed and added successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing documents: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchDocuments(
            @RequestParam String query,
            @RequestParam(defaultValue = "2") int topK,
            @RequestParam(defaultValue = "0.7") double similarityThreshold
    ) {
        try {
            return ResponseEntity.ok(jsonVectorService.searchDocuments(query, topK, similarityThreshold));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error searching documents: " + e.getMessage());
        }
    }

}
