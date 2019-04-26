package ru.itis.teamwork.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WebSocketMessage {
    @JsonProperty("from")
    private String fromUserName;
    @JsonProperty("text")
    private String text;
    @JsonProperty("chat_id")
    private String chatId;
    @JsonIgnoreProperties
    private User user;
}

