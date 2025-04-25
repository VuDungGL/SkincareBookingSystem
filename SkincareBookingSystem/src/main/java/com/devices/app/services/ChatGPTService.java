package com.devices.app.services;

import com.devices.app.config.openAI.OpenAIConfig;
import com.devices.app.dtos.requests.ChatBoxRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
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

        if (chatRequest.getModel() == null || chatRequest.getModel().isEmpty()) {
            chatRequest.setModel("gpt-3.5-turbo"); // Model mặc định
        }

        String body = mapper.writeValueAsString(chatRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(openAIConfig.getApiUrl()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAIConfig.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Trường hợp lỗi 401 hoặc lỗi khác
        if (response.statusCode() != 200) {
            return new ApiResponse<>(response.statusCode(), "Lỗi từ GPT API", response.body());
        }

        JsonNode root = mapper.readTree(response.body());
        JsonNode choices = root.path("choices");

        String gptReply;
        if (choices.isArray() && choices.size() > 0) {
            gptReply = choices.get(0).path("message").path("content").asText();
            return new ApiResponse<>(200, "Thành công", gptReply.trim());
        } else {
            return new ApiResponse<>(200, "Không có phản hồi từ GPT", response.body());
        }
    }

}
