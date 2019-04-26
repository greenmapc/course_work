package ru.itis.teamwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.teamwork.models.User;

import java.awt.*;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE User SET " +
            "firstName = :firstName, lastName = :lastName, " +
            "password = :password, telegramJoined = :telegramJoined, phone = :phone " +
            "WHERE id = :id")
    void settingsUpdate(@Param("firstName") String firstName,
                        @Param("lastName") String lastName,
                        @Param("password") String password,
                        @Param("id") Long id,
                        @Param("telegramJoined") Boolean telegramJoined,
                        @Param("phone") String phone);
}
