package com.example.vector_search_mistral.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.mistralai.MistralAiEmbeddingModel;
import org.springframework.ai.mistralai.MistralAiEmbeddingOptions;
import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MistralAiConfig {

    @Value("${spring.ai.mistralai.api-key}")
    private String apiKey;

    @Value("${spring.ai.mistralai.base-url}")
    private String baseUrl;

    @Bean
    public MistralAiApi mistralAiApi() {
        return new MistralAiApi(baseUrl, apiKey);
    }

    @Bean
    public EmbeddingModel embeddingModel(MistralAiApi mistralAiApi) {
        return new MistralAiEmbeddingModel(mistralAiApi,
                MistralAiEmbeddingOptions.builder()
                        .withModel("mistral-embed").build());
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }
}
