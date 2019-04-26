package ru.itis.teamwork.controllers;

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
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;

import ru.itis.teamwork.models.dto.MessageDto;

import ru.itis.teamwork.models.dto.UserDto;

import ru.itis.teamwork.services.ProjectService;
import ru.itis.teamwork.services.UserService;

import java.util.ArrayList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
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
        Project project = projectService.getProjectById(id);

        if (project != null) {
            model.addAttribute("project", project);
            return "project";
        } else {
            return "redirect:/project";
        }
    }

    @GetMapping("/project/messages/{id}")
    public String messages(@AuthenticationPrincipal User user,
                           @PathVariable("id") String projectId,
                           Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);

        model.addAttribute("user", user);
        if (!user.getTelegramJoined()) {
            model.addAttribute("buttonForm", true);
        }

        Set<User> members = project.getUsers();
        members.removeIf(a -> (a.getTelegramJoined() == null || !a.getTelegramJoined()));

        List<MessageDto> messageDtos = new ArrayList<>();
        if (project.getChat() != null && project.getChat().getMessages() != null) {
            messageDtos = project.getChat().getMessages().stream().map(MessageDto::new).collect(Collectors.toList());
            model.addAttribute("chat", project.getChat());
        }
        model.addAttribute("messages", messageDtos);
        members.remove(user);
        model.addAttribute("members", members);
        if (isMemberOfProject(user, project)) {
            return "projectMessages";
        } else {
            return "redirect:/profile";
        }

    }

    @GetMapping("/project/files/{id}")
    public String files(@AuthenticationPrincipal User user,
                        @PathVariable("id") String projectId,
                        Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        if (isMemberOfProject(user, project)) {
            return "projectFiles";
        } else {
            return "redirect:/profile";
        }
    }

    @GetMapping("/project/tasks/{id}")
    public String tasks(@AuthenticationPrincipal User user,
                        @PathVariable("id") String projectId,
                        Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        if (isMemberOfProject(user, project)) {
            return "projectTasks";
        } else {
            return "redirect:/profile";
        }
    }

    @GetMapping("/project/settings/{id}")
    public String settings(@AuthenticationPrincipal User user,
                           @PathVariable("id") String projectId,
                           Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", projectService.getProjectById(id));
        if (isMemberOfProject(user, project)) {
            return "projectSettings";
        } else {
            return "redirect:/profile";
        }
    }

    @GetMapping("/project/members/{id}")
    public String members(@AuthenticationPrincipal User user,
                          @PathVariable("id") String projectId,
                          Model model) {
        Long id = Long.parseLong(projectId);
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        if (isMemberOfProject(user, project)) {
            if (project.getUsers() != null) {
                model.addAttribute("members", project.getUsers());
            }
            return "projectMembers";
        } else {
            return "redirect:/profile";
        }
    }


    @PostMapping("/project/{projectId}/settings/addMember")
    public String addMember(@PathVariable("projectId") Long projectId,
                            @RequestParam("username") String username,
                            ModelMap modelMap) {
        Project project = projectService.getProjectById(projectId);
        if (!projectService.addMember(project, username)) {
            modelMap.addAttribute("error", "User " + username + " not found");
            modelMap.addAttribute("project", project);
            return "projectMembers";
        }

        return "redirect:/project/members/" + projectId;
    }


    @GetMapping(value = "/show_like_users", produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public @ResponseBody
    List<UserDto> showLikeUsers(@RequestParam String username) {
        return projectService.getUsersLike(username).getUserDtoList();
    }

    private boolean isMemberOfProject(User user, Project project) {
        User userFromDb = userService.getUserById(user.getId()).get();
        return userFromDb.getProjects().contains(project);
    }
}
