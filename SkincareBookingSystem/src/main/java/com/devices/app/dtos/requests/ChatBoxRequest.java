package com.devices.app.dtos.requests;

import com.devices.app.dtos.dto.MessagesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ChatBoxRequest {
    private String model;
    private List<MessagesDto> messages;
}
