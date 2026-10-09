package com.sayedhadi.springaibasicchat.chat.controller;

import com.sayedhadi.springaibasicchat.chat.dto.ChatRequestDto;
import com.sayedhadi.springaibasicchat.chat.dto.ChatResponseDto;
import com.sayedhadi.springaibasicchat.chat.services.ChatOperations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatOperations chatOperations;

    @PostMapping
    public ChatResponseDto chat(@Valid @RequestBody ChatRequestDto request) {
        return ChatResponseDto.builder()
                .reply(chatOperations.chat(request.getMessage()))
                .build();
    }
}
