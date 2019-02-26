package ru.itis.teamwork.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.UserMainImg;

@Repository
public interface UserMainImgRepository extends CrudRepository<UserMainImg, Long> {
}
