# Spring AI + OpenAI Integration Project

A complete AI-powered backend application built with **Spring Boot**, **Spring AI**, and **OpenAI APIs**.

This project demonstrates how to integrate modern AI capabilities into Java applications using the Spring ecosystem. It includes OpenAI model integration, vector database support with PGVector, Retrieval-Augmented Generation (RAG), document ingestion, image generation, and conversational AI workflows.

---

## Features

- OpenAI Chat Model integration using Spring AI
- Image generation using OpenAI image APIs
- Audio transcription support
- Retrieval-Augmented Generation (RAG)
- PGVector vector database integration
- PDF/document ingestion using Apache Tika
- Embedding storage and semantic search
- Docker Compose support for PostgreSQL + PGVector
- Spring Boot REST APIs
- Vector Store Advisors support
- Chunking and embedding pipeline for documents
- Production-ready backend structure

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.4.4
- Spring AI 1.0.8
- Maven

### AI & Vector Search
- OpenAI APIs
- Spring AI OpenAI Starter
- PGVector
- Spring AI Vector Store
- Apache Tika Document Reader

### Database
- PostgreSQL
- PGVector extension

### Dev Tools
- Docker Compose
- Spring Boot Docker Compose support

---

## Configuration

The application uses environment variables for secure API access.

### Required Environment Variable

```env
OPENAI_API_KEY=your_openai_api_key
```

---

## Application Properties

Configured highlights from the project:

```properties
spring.ai.openai.chat.model=gpt-4o-mini
spring.ai.openai.image.options.model=gpt-image-1

spring.ai.vectorstore.pgvector.index-type=hnsw
spring.ai.vectorstore.pgvector.distance-type=cosine_distance
spring.ai.vectorstore.pgvector.dimensions=1536

server.port=8081
```

---

## Running PostgreSQL + PGVector

The project includes Docker Compose support.

### Start Services

```bash
docker compose up -d
```

This starts:
- PostgreSQL
- PGVector extension support

---

## AI Capabilities Included

### 1. Conversational AI

The application integrates OpenAI chat models using Spring AI's `ChatClient` and model abstractions.

### 2. Image Generation

Supports image generation using:

- `gpt-image-1`
- OpenAI Image APIs
- Spring AI image model integrations

### 3. Audio Transcription

Implements audio transcription workflows using OpenAI transcription APIs.

### 4. Retrieval-Augmented Generation (RAG)

The project supports:
- Document ingestion
- Text chunking
- Embedding generation
- Vector similarity search
- Context-aware AI responses

### 5. Vector Database Integration

Uses PGVector with:
- HNSW indexing
- Cosine similarity search
- Efficient embedding retrieval

---

## RAG Pipeline Overview

```text
Documents/PDFs
       ↓
Apache Tika Reader
       ↓
Text Chunking
       ↓
OpenAI Embeddings
       ↓
PGVector Storage
       ↓
Semantic Retrieval
       ↓
LLM Context Injection
       ↓
AI Response
```

---

## Why Document Chunking Matters

Large documents cannot be efficiently embedded or retrieved as a single block.

This project uses chunking to:
- Reduce token usage
- Improve semantic retrieval accuracy
- Lower OpenAI API costs
- Improve context precision
- Enable scalable RAG workflows

## Screenshots

The repository contains a `screenshots/` folder demonstrating application outputs and API interactions.

---

## Learning Highlights

This project demonstrates practical implementation of:

- Spring AI abstractions
- OpenAI integrations in Java
- Vector databases
- Embeddings and semantic search
- RAG architecture
- AI backend engineering
- Dockerized development workflows
- Production-ready application structure