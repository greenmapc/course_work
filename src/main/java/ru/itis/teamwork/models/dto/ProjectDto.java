package ru.itis.teamwork.models.dto;

import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Project;

public class ProjectDto {
    public static Project from(CreateProjectForm form) {
        Project p = new Project();
        p.setName(form.getName());
        p.setDescription(form.getDescription());
        //TODO: сделать добавление тимлида
        return p;
    }

    public static CreateProjectForm from(Project project) {
        CreateProjectForm cp = new CreateProjectForm();
        cp.setName(project.getName());
        cp.setDescription(project.getDescription());
        cp.setTeamLeaderLogin(project.getTeamLeader().getUsername());

        return cp;
    }
}
