package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.creators.CreationProjectService;

import java.security.Principal;
import java.util.Set;

@Controller
public class ProjectController {
    private final CreationProjectService creationProjectService;

    @Autowired
    public ProjectController(CreationProjectService creationProjectService) {
        this.creationProjectService = creationProjectService;
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
        if (bindingResult.hasErrors()) {
            return "creators/newProject";
        }
        form.setTeamLeaderLogin(principal.getName());
        creationProjectService.create(form);
        return "redirect:/projects";
    }

    @GetMapping("/projects")
    public String projects(@AuthenticationPrincipal User user,
                           Model model) {
        Set<Project> projects = user.getProjects();
        for (Project project :
                projects) {
            System.out.println(project);
        }
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/project/{projectId}")
    public String projectOverview(Model model,
                                  @PathVariable String projectId) {

        return "project";
    }
}
