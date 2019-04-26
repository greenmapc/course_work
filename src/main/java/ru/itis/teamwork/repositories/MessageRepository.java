package ru.itis.teamwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
