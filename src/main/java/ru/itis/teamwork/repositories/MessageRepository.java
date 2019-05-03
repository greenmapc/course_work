package ru.itis.teamwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.Chat;
import ru.itis.teamwork.models.Message;
import ru.itis.teamwork.models.User;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Set<Message> findAllByChatOrderByDate(Chat chat);
    Optional<Message> findBySenderAndTextAndTimestamp(User user, String str, Float timestamp);
}

