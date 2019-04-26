package ru.itis.teamwork.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StringsToUserConverter implements Converter<Set<String>, Set<User>> {
    @Autowired
    private UserService userService;

    @Override
    public Set<User> convert(Set<String> strings) {
        List<Long> ids = strings.stream().map(a -> {
            try {
                return Long.parseLong(a);
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList());
        return userService.getUsers(ids);
    }
}
