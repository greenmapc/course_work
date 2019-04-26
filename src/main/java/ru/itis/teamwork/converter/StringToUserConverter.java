package ru.itis.teamwork.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.UserService;

import java.util.Optional;

@Component
public class StringToUserConverter implements Converter<String, User> {
    @Autowired
    private UserService userService;

    @Override
    public User convert(String s) {
        Long id = Long.parseLong(s);
        Optional<User> user = userService.getUserById(id);
        return user.orElse(null);
    }
}
