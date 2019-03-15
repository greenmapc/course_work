package ru.itis.teamwork.forms;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotBlank;

import ru.itis.teamwork.models.Roles;
import ru.itis.teamwork.util.validAnnotation.FieldMatch;

import javax.validation.constraints.Pattern;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
@FieldMatch.List({
        @FieldMatch(firstField = "password", secondField = "repeatPassword")
})
public class SignUpForm extends Form {

    @NotBlank(message = "{emptyField}")
    private String firstName;

    @NotBlank(message = "{emptyField}")
    private String lastName;

    @Pattern(regexp = "[A-Za-z0-9_]{3,16}", message = "{username.incorrect}")
    private String username;

    @Pattern(regexp = "[A-Za-z0-9_]{6,16}", message = "{password.incorrect}")
    private String password;

    private String repeatPassword;

    private Set<Roles> roles;
}
