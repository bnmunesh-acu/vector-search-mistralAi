package com.example.vector_search_mistral.controller;

import com.example.vector_search_mistral.service.VectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/docs")
@RequiredArgsConstructor
public class VectorController {
    private final VectorService vectorService;

    @GetMapping("")
    public List<Map<String, Object>> getAllDocuments() {
        return vectorService.getAllProcessedDocuments();
    }

    @PostMapping("/create")
    public ResponseEntity<String> addDocuments(@RequestBody List<Document> documents) {
        try {
            vectorService.processAndAddDocuments(documents);
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
            return ResponseEntity.ok(vectorService.searchDocuments(query, topK, similarityThreshold));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error searching documents: " + e.getMessage());
        }
    }

    //get only the content
    @GetMapping("/search2")
    public ResponseEntity<?> searchDocuments2(
            @RequestParam String query,
            @RequestParam(defaultValue = "2") int topK,
            @RequestParam(defaultValue = "0.7") double similarityThreshold
    ) {
        try {
            return ResponseEntity.ok(vectorService.searchDocuments(query, topK, similarityThreshold).get(0).getContent().toString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error searching documents: " + e.getMessage());
        }
    }
}
