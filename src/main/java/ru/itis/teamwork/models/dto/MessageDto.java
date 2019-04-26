package ru.itis.teamwork.models.dto;

import lombok.Data;
import ru.itis.teamwork.models.Message;

@Data
public class MessageDto {

    public MessageDto (Message message){
        this.text = message.getText();
        this.date = message.getDate().toString();
        this.senderUserName = message.getSender().getUsername();
    }

    private String text;
    private String senderUserName;
    private String date;
}
