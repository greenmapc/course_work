package ru.itis.teamwork.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.teamwork.models.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembersDto {
    private List<User> userList;

    private List<UserDto> userDtoList;
}
