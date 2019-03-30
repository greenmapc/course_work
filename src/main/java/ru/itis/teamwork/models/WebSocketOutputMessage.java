package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class WebSocketOutputMessage {

    private String userName;
    private String text;
    private String dateTime;


}
