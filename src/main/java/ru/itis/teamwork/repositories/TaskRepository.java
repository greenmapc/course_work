package ru.itis.teamwork.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
}
