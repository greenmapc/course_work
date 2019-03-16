package ru.itis.teamwork.services.creators;

import ru.itis.teamwork.forms.Form;

public interface CreationService<T extends Form> {
    boolean create(T form);
}
