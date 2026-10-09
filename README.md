# Spring AI Basic Chat

A deliberately small first AI project: a Spring Boot REST API that sends a user message to a local Ollama model through Spring AI and returns the model's reply.

## What this project teaches

- How an application sends a prompt to an LLM
- The difference between a system message and a user message
- How Spring AI's `ChatClient` hides provider-specific HTTP details
- How request and response DTOs define the API shape, with Lombok generating common methods
- How to connect a local model to a Spring application
- How to test the web layer without making a paid model call

## Request flow

```text
HTTP client -> chat/controller -> chat/services/impl -> chat/config/ai -> Ollama (local model)
```

The first version is intentionally stateless. Each request is independent and the model does not remember earlier messages.

## Prerequisites

- Java 25
- [Ollama](https://ollama.com/download) installed and running
- The `qwen3:4b` model downloaded (about 2.5 GB)

The Maven wrapper is included, so a separate Maven installation is not required.

## Run the application

1. Install and start Ollama. Then download the model:

   ```bash
   ollama run qwen3:4b
   ```

   The first run downloads the model. Once it is ready, you can exit the interactive Ollama prompt; Ollama continues serving locally on port `11434`.

2. Start Spring Boot:

   ```bash
   ./mvnw spring-boot:run
   ```

3. In another terminal, send a message:

   ```bash
   curl --request POST http://localhost:8080/api/chat \
     --header 'Content-Type: application/json' \
     --data '{"message":"Explain dependency injection in one paragraph."}'
   ```

The response has a small, stable JSON shape:

```json
{"reply":"...model response..."}
```

Blank messages and messages longer than 2,000 characters return HTTP 400 with a JSON error, for example `{"error":"Message must not be blank"}`.

The app sends requests to Ollama at `http://localhost:11434`, using `qwen3:4b`. These requests run on your computer and do not use OpenAI API billing. The model download uses disk space, and generation speed depends on your computer.

## Run the tests

```bash
./mvnw test
```

The controller test provides a stub chat operation. It verifies our API contract and validation without contacting Ollama or using paid API tokens. The application context test also does not send a model request.

## Read the code in this order

1. `application.properties` — external model configuration
2. `chat/controller` — HTTP endpoint
3. `chat/dto` — request, success response, and error response shapes
4. `chat/exceptions` — validation error handling
5. `chat/services` and `chat/services/impl` — chat service contract and implementation
6. `chat/config/ai` — Spring AI client configuration
7. `src/test/.../chat/controller/ChatControllerTests` — isolated web-layer behavior

## Suggested learning milestones

1. **Basic chat (this version):** one request, one response
2. **Prompt experiments:** change the system message and compare results
3. **Conversation memory:** add a conversation ID and retain recent messages
4. **Structured output:** ask the model to return a Java record rather than free text
5. **Amazon Bedrock:** switch the provider while keeping the controller contract
6. **Production basics:** error handling, timeouts, observability, and deployment to AWS

Do one milestone at a time and publish what changed, what you learned, and one limitation you discovered.

## Prompt experiment

With the application running, change the default system message in `ChatClientConfiguration`, restart, and ask the same question each time. Compare these instructions:

- `Answer like a senior Java developer.`
- `Explain the answer to a beginner.`
- `Reply in exactly three bullet points.`

The system message sets the assistant's general behavior; the user message contains the specific question. Note what changes and where the answer still misses the instruction.
