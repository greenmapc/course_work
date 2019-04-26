package ru.itis.teamwork.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.teamwork.models.Message;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.WebSocketMessage;
import ru.itis.teamwork.models.WebSocketOutputMessage;
import ru.itis.teamwork.repositories.ChatRepository;
import ru.itis.teamwork.repositories.MessageRepository;
import ru.itis.teamwork.services.TelegramService;
import ru.itis.teamwork.services.UserService;

import java.io.IOException;
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

    @Autowired
    private TelegramService telegramService;



    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/messages/{chatId}")
    public WebSocketOutputMessage send(@DestinationVariable String chatId, WebSocketMessage message) throws IOException {

        Date date = new Date();
        String time = new SimpleDateFormat("HH:mm").format(date);
        WebSocketOutputMessage webSocketOutputMessage =  WebSocketOutputMessage.builder()
                .dateTime(time)
                .text(message.getText())
                .username(message.getFromUserName())
                .build();
        User user = (User) userService.loadUserByUsername(message.getFromUserName());
        Message chatMessage = Message.builder()
                .chat(chatRepository.findById(Long.parseLong(chatId)).get())
                .date(date)
                .sender(user)
                .text(message.getText())
                .build();
        messageRepository.save(chatMessage);

        telegramService.sendMessage(chatMessage);
        return webSocketOutputMessage;
    }

    @GetMapping("/chat")
    public String get() {
        return "chat";
    }
}
