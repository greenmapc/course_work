package ru.itis.teamwork.controllers;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.parser.JSONParser;
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
import ru.itis.teamwork.models.dto.UserDto;
import ru.itis.teamwork.services.ProjectService;
import ru.itis.teamwork.services.UserService;

import javax.json.Json;
import javax.ws.rs.Produces;
import java.util.ArrayList;
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
        model.addAttribute("user", user);
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
    public String messages(@AuthenticationPrincipal User user,
                           @PathVariable("id") String projectId,
                           Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        //if (isMemberOfProject(user, project)) {
        return "projectMessages";
        //} else {
        //    return "redirect:/profile";
        //}
    }

    @GetMapping("/project/files/{id}")
    public String files(@AuthenticationPrincipal User user,
                        @PathVariable("id") String projectId,
                        Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        //if (isMemberOfProject(user, project)) {
        return "projectFiles";
        //} else {
        //    return "redirect:/profile";
        //}
    }

    @GetMapping("/project/tasks/{id}")
    public String tasks(@AuthenticationPrincipal User user,
                        @PathVariable("id") String projectId,
                        Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        //if (isMemberOfProject(user, project)) {
        return "projectTasks";
        //} else {
        //    return "redirect:/profile";
        //}
    }

    @GetMapping("/project/settings/{id}")
    public String settings(@AuthenticationPrincipal User user,
                           @PathVariable("id") String projectId,
                           Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", projectService.getProjectById(id));
        //if (isMemberOfProject(user, project)) {
        return "projectSettings";
        //} else {
        //    return "redirect:/profile";
        //}
    }

    @GetMapping("/project/members/{id}")
    public String members(@AuthenticationPrincipal User user,
                          @PathVariable("id") String projectId,
                          Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        //if (isMemberOfProject(user, project)) {
        return "projectMembers";
        //} else {
        //    return "redirect:/profile";
        //}
    }

    private boolean isMemberOfProject(User user, Project project) {
        return user.getProjects().contains(project);
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
    @SneakyThrows
    public @ResponseBody List<UserDto> showLikeUsers(@RequestParam String username) {
        List<UserDto> users = projectService.getUsersLike(username).getUserDtoList();
        return users;
    }

}
