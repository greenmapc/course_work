package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.ProjectService;

import java.util.Set;

@Controller
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/newProject")
    public String addProjectPage(Model model,
                                 @AuthenticationPrincipal User user) {
        model.addAttribute("form", new CreateProjectForm());
        model.addAttribute("user", user);
        model.addAttribute("isCurrentUser", true);
        return "creators/newProject";
    }

    @PostMapping("/newProject")
    public String addProject(@Validated @ModelAttribute("form") CreateProjectForm form,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal User user,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("isCurrentUser", true);
            return "creators/newProject";
        }
        form.setTeamLeaderLogin(user.getUsername());
        if (projectService.create(form)) {
            return "redirect:/profile";
        } else {
            return "creators/newProject";
        }
    }

    @GetMapping("/projects")
    public String projects(@AuthenticationPrincipal User user,
                           Model model) {
        Set<Project> projects = user.getProjects();
        for (Project project :
                projects) {
            System.out.println(project);
        }
        model.addAttribute("isCurrentUser", true);
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/project/{projectId}")
    public String projectOverview(Model model,
                                  @PathVariable String projectId,
                                  @AuthenticationPrincipal User user) {
        Long id = Long.parseLong(projectId);
        model.addAttribute("project", projectService.getProjectById(id));
        return "project";
    }

    @GetMapping("/project/messages/{id}")
    public String messages(@PathVariable("id") String projectId,
                           Model model) {
        Long id = Long.parseLong(projectId);
        model.addAttribute("project", projectService.getProjectById(id));
        return "projectMessages";
    }

    @GetMapping("/project/files/{id}")
    public String files(@PathVariable("id") String projectId,
                        Model model) {
        Long id = Long.parseLong(projectId);
        model.addAttribute("project", projectService.getProjectById(id));
        return "projectFiles";
    }

    @GetMapping("/project/tasks/{id}")
    public String tasks(@PathVariable("id") String projectId,
                        Model model) {
        Long id = Long.parseLong(projectId);
        model.addAttribute("project", projectService.getProjectById(id));
        return "projectTasks";
    }

    @GetMapping("/project/settings/{id}")
    public String settings(@PathVariable("id") String projectId,
                           Model model) {
        Long id = Long.parseLong(projectId);
        model.addAttribute("project", projectService.getProjectById(id));
        return "projectSettings";
    }

    @GetMapping("/project/members/{id}")
    public String members(@PathVariable("id") String projectId,
                          Model model) {
        Long id = Long.parseLong(projectId);
        model.addAttribute("project", projectService.getProjectById(id));
        return "projectMembers";
    }
}
