package com.jane.spring_ai_open_ai.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

//@Configuration
public class VectorLoader {

    private static final Logger log = LoggerFactory.getLogger(VectorLoader.class);

    @Value("classpath:/India_Constitution.pdf")
    private Resource pdfResource;
    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = new File("C:\\Users\\rituj\\Documents\\Coding\\springAI-all\\spring-ai-open-ai\\src\\main\\resources\\vector_file.json");

        System.out.println("vectorStoreFile = " + vectorStoreFile);
        if (vectorStoreFile.exists()) {
            log.info("vectorStoreFile exists");
            store.load(vectorStoreFile);
        } else {
            log.info("Creating Vector Store!");

            TikaDocumentReader reader =
                    new TikaDocumentReader(pdfResource);

            TokenTextSplitter splitter =
                    TokenTextSplitter.builder()
                            .withChunkSize(256)
                            .withMaxNumChunks(1000)
                            .build();

            List<Document> docs =
                    splitter.apply(reader.get());

            store.add(docs);

            store.save(vectorStoreFile);

            log.info("Vector Store Created Successfully");
        }
        return store;
    }
}
