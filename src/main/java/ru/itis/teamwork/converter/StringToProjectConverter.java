package ru.itis.teamwork.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.services.ProjectService;


public class StringToProjectConverter implements Converter<String, Project> {
    private ProjectService projectService;

    @Autowired
    public StringToProjectConverter(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public Project convert(String id) {
        return projectService.getProjectById(Long.parseLong(id));
    }
}
