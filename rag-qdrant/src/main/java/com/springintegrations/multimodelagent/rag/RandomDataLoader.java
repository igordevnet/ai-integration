package com.springintegrations.multimodelagent.rag;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RandomDataLoader {

    private final VectorStore vectorStore;

    public RandomDataLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadSentencesIntoVectorStore() {
        List<String> sentences = List.of(
                // Java and Spring
                "Java is an object-oriented programming language widely used for backend development.",
                "Spring Boot simplifies the development of Java applications by providing auto-configuration.",
                "Dependency injection allows objects to receive their dependencies instead of creating them directly.",
                "Spring AI provides abstractions for integrating language models and vector databases into Java applications.",
                "REST APIs allow different applications to communicate over HTTP.",

                // Artificial Intelligence
                "Artificial intelligence enables computers to perform tasks that typically require human intelligence.",
                "Large language models are trained on massive datasets to understand and generate human language.",
                "Embeddings transform text into numerical vectors that represent semantic meaning.",
                "Vector databases store embeddings and enable similarity searches based on semantic relationships.",
                "Retrieval Augmented Generation combines external knowledge retrieval with language model generation.",

                // Databases
                "Relational databases organize information into tables with rows and columns.",
                "MongoDB is a document-oriented database that stores data in flexible JSON-like documents.",
                "PostgreSQL supports complex queries, transactions, and advanced relational database features.",
                "Database indexes improve query performance by reducing the amount of data that must be scanned.",
                "Qdrant is a vector database designed to store and search high-dimensional vectors.",

                // Distributed Systems
                "Microservices architecture divides an application into smaller independently deployable services.",
                "Message brokers allow distributed services to communicate asynchronously.",
                "Redis is an in-memory data store commonly used for caching and real-time applications.",
                "Kubernetes orchestrates containers and helps manage distributed applications.",
                "Event-driven architecture allows services to react to events without being tightly coupled.",

                // Software Engineering
                "Clean Architecture separates business rules from infrastructure and external frameworks.",
                "The SOLID principles encourage maintainable and extensible object-oriented software.",
                "Unit tests verify the behavior of individual components in isolation.",
                "Caching reduces repeated database queries and improves application response times.",
                "Authentication verifies the identity of a user before granting access to protected resources."
        );

        List<Document> documents = sentences.stream().map(Document::new).toList();

        List<Document> response = vectorStore.similaritySearch(sentences.getFirst());

        if (response.isEmpty()) {
            vectorStore.add(documents);
        }
    }
}
