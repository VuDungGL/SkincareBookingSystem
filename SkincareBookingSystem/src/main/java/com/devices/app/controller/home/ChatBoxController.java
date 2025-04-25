package com.devices.app.controller.home;

import com.devices.app.dtos.requests.ChatBoxRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.services.ChatGPTService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/chatbox")
public class ChatBoxController {
    private final ChatGPTService chatGPTService;

    public ChatBoxController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @PostMapping("/sendMessage")
    public ApiResponse<String> sendMessage(@RequestBody ChatBoxRequest chatRequest) throws IOException, InterruptedException {
        return chatGPTService.sendMessage(chatRequest);
    }
}
