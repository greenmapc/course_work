package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebSocketOutputMessage {
    private String username;
    private String text;
    private String dateTime;
}
