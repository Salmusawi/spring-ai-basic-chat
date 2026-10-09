package com.sayedhadi.springaibasicchat.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatRequestDto {

    @NotBlank(message = "Message must not be blank")
    @Size(max = 2_000, message = "Message must not exceed 2000 characters")
    private String message;
}
