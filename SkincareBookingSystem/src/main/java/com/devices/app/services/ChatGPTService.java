package com.devices.app.services;

import com.devices.app.config.openAI.OpenAIConfig;
import com.devices.app.dtos.requests.ChatBoxRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ChatGPTService {
    private final OpenAIConfig openAIConfig;

    public ChatGPTService(OpenAIConfig openAIConfig) {
        this.openAIConfig = openAIConfig;
    }

    public ApiResponse<String> sendMessage(ChatBoxRequest chatRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(chatRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(openAIConfig.getApiUrl()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAIConfig.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse<>(response.statusCode(), "Thành công", response.body());
    }

}
