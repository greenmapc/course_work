package ru.itis.teamwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
