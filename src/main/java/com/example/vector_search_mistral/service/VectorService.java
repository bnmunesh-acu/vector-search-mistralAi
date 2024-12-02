package com.example.vector_search_mistral.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VectorService {
    private final VectorStore vectorStore;
    private List<Document> processedDocuments;

    @Autowired
    public VectorService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.processedDocuments = new ArrayList<>();
    }

    public List<Map<String, Object>> getAllProcessedDocuments() {
        return processedDocuments.stream()
                .map(doc -> Map.of(
                        "content", doc.getContent(),
                        "metadata", doc.getMetadata(),
                        "id", doc.getId()
                ))
                .collect(Collectors.toList());
    }

    public void processAndAddDocuments(List<Document> documents) {
        for (Document doc : documents) {
            try {
                // Add the document
                vectorStore.add(Collections.singletonList(doc));
                processedDocuments.add(doc);

                log.info("Added document: {}", doc.getContent());
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                log.error("Error adding document: {}", doc.getContent(), e);
                if (processedDocuments.size() < documents.size()) {
                    log.warn("Stopping document processing due to error");
                    break;
                }
            }
        }
        log.info("Successfully processed {} out of {} documents", processedDocuments.size(), documents.size());
    }

    public List<Document> searchDocuments(String query, int topK, double similarityThreshold) {
        return vectorStore.similaritySearch(
                SearchRequest.defaults()
                        .withQuery(query)
                        .withTopK(topK)
                        .withSimilarityThreshold(similarityThreshold)
        );
    }
}
