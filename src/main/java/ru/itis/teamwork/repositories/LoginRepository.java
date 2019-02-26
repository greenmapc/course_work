package ru.itis.teamwork.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.Login;

@Repository
public interface LoginRepository extends CrudRepository<Login, Long> {
}
