package com.sayedhadi.springaibasicchat.chat.controller;

import com.sayedhadi.springaibasicchat.chat.services.ChatOperations;
import com.sayedhadi.springaibasicchat.chat.exceptions.ChatExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
@Import({ChatControllerTests.TestConfig.class, ChatExceptionHandler.class})
class ChatControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StubChatOperations chatOperations;

    @Test
    void returnsTheAssistantReply() throws Exception {
        chatOperations.reply = "Spring AI connects Spring applications to AI models.";

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"message":"What is Spring AI?"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply")
                        .value("Spring AI connects Spring applications to AI models."));

        assertThat(chatOperations.lastMessage)
                .isEqualTo("What is Spring AI?");
    }

    @Test
    void rejectsABlankMessage() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"message":"   "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Message must not be blank"));
    }

    @Test
    void rejectsAMessageLongerThanTwoThousandCharacters() throws Exception {
        String longMessage = "a".repeat(2_001);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"message":"%s"}
                                """.formatted(longMessage)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Message must not exceed 2000 characters"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        StubChatOperations chatOperations() {
            return new StubChatOperations();
        }
    }

    static class StubChatOperations implements ChatOperations {

        private String reply = "Stub reply";
        private String lastMessage;

        @Override
        public String chat(String message) {
            lastMessage = message;
            return reply;
        }
    }
}
