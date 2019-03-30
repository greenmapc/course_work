package ru.itis.teamwork.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class WebSocketMessage {

    @JsonProperty("from_phone")
    private String fromPhone;
    @JsonProperty("text")
    private String text;
    @JsonProperty("to_phone")
    private String toPhone;
    @JsonIgnoreProperties
    private String userName;
}

