package ru.itis.teamwork.forms;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.itis.teamwork.models.User;

import java.util.Set;


@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class CreateProjectForm extends Form {
    private String name;
    private User teamLeaderLogin;
    private Set<User> participants;
    private String description;
}
