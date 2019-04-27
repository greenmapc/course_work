package ru.itis.teamwork.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.teamwork.models.Message;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.WebSocketMessage;
import ru.itis.teamwork.models.WebSocketOutputMessage;
import ru.itis.teamwork.services.MessageService;
import ru.itis.teamwork.services.RabbitService;
import ru.itis.teamwork.services.TelegramService;
import ru.itis.teamwork.services.UserService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RabbitService rabbitService;

    @PostConstruct
    private void setMessagingTemplateToRabbit(){
        this.rabbitService.setMessagingTemplate(messagingTemplate);
    }

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
        Message chatMessage = messageService.createAndSaveMessage(Long.parseLong(chatId), user, message.getText(), date);

        telegramService.sendMessage(chatMessage);
        return webSocketOutputMessage;
    }

    @GetMapping("/chat")
    public String get() {
        return "chat";
    }
}
