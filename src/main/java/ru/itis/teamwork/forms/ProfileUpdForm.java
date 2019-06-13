package ru.itis.teamwork.forms;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.UserMainImg;
import ru.itis.teamwork.util.validAnnotation.FieldMatch;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
@FieldMatch.List({
        @FieldMatch(firstField = "password", secondField = "repeatPassword")
})
public class ProfileUpdForm extends Form {

    @NotEmpty(message = "{emptyField}")
    private String firstName;

    @NotEmpty(message = "{emptyField}")
    private String lastName;

    private String password;

    private String repeatPassword;

    public static ProfileUpdForm buildFormByUser(User user) {
        ProfileUpdForm profileUpdForm = new ProfileUpdForm();

        profileUpdForm.setFirstName(user.getFirstName());
        profileUpdForm.setLastName(user.getLastName());

        return profileUpdForm;
    }

}
