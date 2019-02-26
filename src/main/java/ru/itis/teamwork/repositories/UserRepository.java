package ru.itis.teamwork.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
