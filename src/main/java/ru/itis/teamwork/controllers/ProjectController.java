package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.creators.CreationProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ProjectController {
    private final CreationProjectService projectService;

    @Autowired
    public ProjectController(CreationProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/addProject")
    public String addProjectPage() {
        return "addProject";
    }

    @PostMapping("/addProject")
    public String addProject(@RequestParam String name,
                             @RequestParam String description,
                             Model model) {
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
