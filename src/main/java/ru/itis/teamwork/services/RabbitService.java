package ru.itis.teamwork.services;

import lombok.Data;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.WebSocketOutputMessage;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Data
public class RabbitService {

    private final Logger logger = Logger.getLogger(RabbitService.class.getName());

    //    устанавливается в websocketcontroller потому что иначе дает другой экземпляр
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpleMessageListenerContainer container;

    @PostConstruct
    public void startMessageProcessing() {
        container.setMessageListener(message -> {
            logger.info("received from queue1 : " + new String(message.getBody()));
            try {
                JSONObject jsonMessage = new JSONObject(new String(message.getBody()));

                Long chatId = jsonMessage.getLong("chat_id");
                Float timestamp = jsonMessage.getFloat("timestamp");
                Optional<User> userOptional = userService.getUserByTelegramId(jsonMessage.getLong("sender_id"));
                String text = jsonMessage.getString("text");
                if (userOptional.isPresent()) {
                    Optional<ru.itis.teamwork.models.Message> messageOptional = messageService.
                            getBySenderAndTextAndTimestamp(userOptional.get(), text, timestamp);

                    if (! messageOptional.isPresent()){

                        ru.itis.teamwork.models.Message chatMessage = messageService.createAndSaveMessage(
                                chatId, userOptional.get(), text, new Date(), timestamp);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm dd-MM-yyyy");
                        WebSocketOutputMessage webSocketOutputMessage = WebSocketOutputMessage.builder()
                                .dateTime(simpleDateFormat.format(chatMessage.getDate()))
                                .text(text)
                                .username(userOptional.get().getUsername())
                                .build();
                        String path = String.format("/topic/messages/%s", chatId);
                        messagingTemplate.convertAndSend(path, webSocketOutputMessage);

                    }
                }
            } catch (Exception e) {
                logger.warning("Unsupported message " + new String(message.getBody()));
            }
        });
    }
}
