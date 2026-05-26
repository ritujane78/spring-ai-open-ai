package com.jane.spring_ai_open_ai.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class PGVectorLoader {
    private static final Logger log = LoggerFactory.getLogger(VectorLoader.class);

    @Value("classpath:/India_Constitution.pdf")
    private Resource pdfResource;

    private final VectorStore vectorStore;
    private final JdbcClient jdbcClient;

    public PGVectorLoader(VectorStore vectorStore, JdbcClient jdbcClient) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
    }
    @PostConstruct
    public void init(){
        Integer count = jdbcClient
                .sql("select count(*) from vector_store")
                .query(Integer.class)
                .single();

        log.info("Number of records in PG vector store: {}", count);

        if(count == 0){
            log.info("Initializing PG vector store Load!");
            TikaDocumentReader reader =
                    new TikaDocumentReader(pdfResource);

            TokenTextSplitter splitter =
                    TokenTextSplitter.builder()
                            .withChunkSize(256)
                            .withMaxNumChunks(1000)
                            .build();
        vectorStore.accept(splitter.apply(reader.get()));

        log.info("PG vector store Load!");

        }

    }
}
