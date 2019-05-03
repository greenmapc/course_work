package ru.itis.teamwork.models.dto;

import lombok.Data;
import ru.itis.teamwork.models.Message;

import java.text.SimpleDateFormat;

@Data
public class MessageDto {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm dd-MM-yyyy");

    public MessageDto (Message message){
        this.text = message.getText();
        this.date = simpleDateFormat.format(message.getDate());
        this.senderUserName = message.getSender().getUsername();
    }

    private String text;
    private String senderUserName;
    private String date;
}
