package com.sayedhadi.springaibasicchat.chat.services.impl;

import com.sayedhadi.springaibasicchat.chat.services.ChatOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService implements ChatOperations {

    private final ChatClient chatClient;

    @Override
    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
