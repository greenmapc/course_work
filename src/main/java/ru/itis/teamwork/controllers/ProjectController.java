package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.repositories.UserRepository;
import ru.itis.teamwork.services.creators.CreationProjectService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectController {
    private final CreationProjectService projectService;

    @Autowired
    public ProjectController(CreationProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/newProject")
    public String addProjectPage(Model model) {
        model.addAttribute("form", new CreateProjectForm());
        return "creators/newProject";
    }

    @PostMapping("/newProject")
    public String addProject(@Validated @ModelAttribute("form") CreateProjectForm form,
                             BindingResult bindingResult,
                             Principal principal) {
        if(bindingResult.hasErrors()) {
            return "creators/newProject";
        }

        form.setTeamLeaderLogin(principal.getName());
        projectService.create(form);
        return "projects";
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        Project project1 = Project.builder()
                .id(1l)
                .name("course-work")
                .build();
        Project project2 = Project.builder()
                .id(2l)
                .name("java-work")
                .build();
        Project project3 = Project.builder()
                .id(3l)
                .name("spring-twitter")
                .build();
        Project project4 = Project.builder()
                .id(4l)
                .name("hometask")
                .build();
        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projects.add(project4);
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/project/{projectId}")
    public String projectOverview(Model model,
                                  @PathVariable String projectId) {
        System.out.println(projectId);
        model.addAttribute("project", Project.builder()
                .id(4l)
                .name("hometask")
                .build());
        return "project";
    }
}
