package ru.itis.teamwork.forms;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Set;


@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CreateProjectForm extends Form {

    @NotBlank(message = "{field.empty}")
    private String name;

    private String teamLeaderLogin;

    @NotBlank(message = "{field.empty}")
    private String description;

}
