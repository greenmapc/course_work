package ru.itis.teamwork.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.itis.teamwork.models.User;

import java.util.List;

@Data
@AllArgsConstructor
public class MembersDto {
    private List<User> userList;
}
