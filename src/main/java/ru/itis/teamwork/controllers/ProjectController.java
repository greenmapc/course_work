package ru.itis.teamwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.dto.MembersDto;
import ru.itis.teamwork.services.ProjectService;

import java.util.List;
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
        Project project = projectService.create(form);
        if (project != null) {
            user.getProjects().add(project);
            model.addAttribute("project", project);
            return "redirect:/project/" + project.getId();
        } else {
            return "creators/newProject";
        }
    }

    @GetMapping("/projects")
    public String projects(@AuthenticationPrincipal User user,
                           Model model) {
        Set<Project> projects = user.getProjects();
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

    @PostMapping("/project/{projectId}/settings/addMember")
    public String addMember(@PathVariable("projectId") Long projectId,
                            @RequestParam("username") String username,
                            ModelMap modelMap) {
        Project project = projectService.getProjectById(projectId);

        if(!projectService.addMember(project, username)) {
            modelMap.addAttribute("error", "User " + username + " not found");
            modelMap.addAttribute("project", project);
            return "projectMembers";
        }

        return "redirect: " + MvcUriComponentsBuilder.fromMappingName("PC#members").arg(0, String.valueOf(projectId)).build();
    }


    @GetMapping(value="/show_like_users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @SneakyThrows
    public List<User> showLikeUsers(@RequestParam String username) {
//        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("ASD");
        return projectService.getUsersLike(username).getUserList();
    }

}
