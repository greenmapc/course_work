package ru.itis.teamwork.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.teamwork.models.Message;
import ru.itis.teamwork.models.WebSocketMessage;
import ru.itis.teamwork.models.WebSocketOutputMessage;
import ru.itis.teamwork.repositories.ChatRepository;
import ru.itis.teamwork.repositories.MessageRepository;
import ru.itis.teamwork.services.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/messages/{chatId}")
    public WebSocketOutputMessage send(@DestinationVariable String chatId, WebSocketMessage message) {

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        WebSocketOutputMessage webSocketOutputMessage =  WebSocketOutputMessage.builder()
                .dateTime(time)
                .text(message.getText())
                .username(message.getFromUserName())
                .build();
        System.out.println(webSocketOutputMessage);

        Message chatMessage = Message.builder().chat(chatRepository.findById(Long.parseLong(chatId)).get()).build();

        return webSocketOutputMessage;
    }

    @GetMapping("/chat")
    public String get() {
        return "chat";
    }
}
