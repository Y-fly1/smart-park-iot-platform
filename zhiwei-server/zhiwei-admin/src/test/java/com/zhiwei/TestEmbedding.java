package com.zhiwei;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestEmbedding {

    @Autowired
    private VectorStore vectorStore;

    @Test
    public void addDocument(){
        List<Document> docs = List.of(
                new Document("111",
                        Map.of("category", "db", "type", "vector")),

                new Document("2222",
                        Map.of("category", "framework", "type", "ai")),

                new Document("RAG",
                        Map.of("category", "llm", "type", "rag"))
        );
        vectorStore.add(docs);
    }
}
