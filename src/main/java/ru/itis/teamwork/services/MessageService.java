package ru.itis.teamwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.models.Chat;
import ru.itis.teamwork.models.Message;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.repositories.ChatRepository;
import ru.itis.teamwork.repositories.MessageRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    public Message createAndSaveMessage(Long chatId, User user, String text, Date date){
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            Message chatMessage = ru.itis.teamwork.models.Message.builder()
                    .chat(chat.get())
                    .date(date)
                    .sender(user)
                    .text(text)
                    .build();
            messageRepository.save(chatMessage);
            return chatMessage;
        }
        return null;
    }
}
