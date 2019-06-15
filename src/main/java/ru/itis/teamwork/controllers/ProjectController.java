package ru.itis.teamwork.controllers;

import org.hibernate.JDBCException;
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
import ru.itis.teamwork.models.dto.UserDto;
//import ru.itis.teamwork.services.GitHubService;
import ru.itis.teamwork.services.ProjectService;
import ru.itis.teamwork.services.UserService;
//import ru.itis.teamwork.util.githubApi.GitHubScope;
//import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ProjectController {
    private ProjectService projectService;
    private UserService userService;
//    private GitHubService gitHubService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             UserService userService
//            ,
//                             GitHubService gitHubService
    ) {
        this.projectService = projectService;
        this.userService = userService;
//        this.gitHubService = gitHubService;
    }

    @GetMapping("/newProject")
    public String addProjectPage(Model model,
                                 @AuthenticationPrincipal User user) {
        model.addAttribute("form", new CreateProjectForm());
        model.addAttribute("user", user);

        return "creators/newProject";
    }

    @PostMapping("/newProject")
    @Transactional
    public String addProject(@Validated @ModelAttribute("form") CreateProjectForm form,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal User user,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "creators/newProject";
        }

        form.setTeamLeaderLogin(user.getUsername());

        try {
            Project project = projectService.create(form);
            user.getProjects().add(project);
            userService.saveUser(user);

            model.addAttribute("project", project);
            return "redirect:/project/" + project.getId();

        } catch (JDBCException e) {
            return "creators/newProject";
        }
    }

    @GetMapping("/profile/{username}/projects")
    public String projects(@AuthenticationPrincipal User user,
                           @PathVariable String username,
                           Model model) {
        model.addAttribute("user", userService.compareUser(user, username));
        model.addAttribute("isCurrentUser", username.equals(user.getUsername()));

        return "projects";
    }

    @GetMapping("/project/{id}")
    public String projectOverview(Model model,
                                  @PathVariable Long id,
                                  @AuthenticationPrincipal User user) {
        Optional<Project> projectCandidate = projectService.findById(id);
        if (projectCandidate.isPresent()) {
            model.addAttribute("project", projectCandidate.get());
            return "project";
        } else {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("UC#projects").arg(1, user.getUsername()).build();
        }
    }

    @GetMapping("/project/messages/{id}")
    public String messages(@AuthenticationPrincipal User user,
                           @PathVariable Long id,
                           Model model) {

        Optional<Project> projectCandidate = projectService.findById(id);
        if (!projectCandidate.isPresent() || !isMemberOfProject(user, projectCandidate.get())) {
            return "redirect:/profile/" + user.getUsername();
        }

        Project project = projectCandidate.get();
        model.addAttribute("project", project);
        model.addAttribute("user", user);

        if (user.getTelegramJoined()) {
            Set<User> members = projectService.getTelegramJoinedUser(project);
            members.remove(user);
            model.addAttribute("members", members);

            model.addAttribute("chat", project.getChat());
            model.addAttribute("messages", projectService.getProjectMessages(project));

        }
        return "projectMessages";
    }

    @GetMapping("/project/files/{id}")
    public String files(@AuthenticationPrincipal User user,
                        @PathVariable Long id,
                        Model model) {
        Optional<Project> projectCandidate = projectService.findById(id);
        if (projectCandidate.isPresent() && isMemberOfProject(user, projectCandidate.get())) {
            model.addAttribute("project", projectCandidate.get());
            return "projectFiles";
        } else {
            return "redirect:/profile/" + user.getUsername();
        }
    }

    @GetMapping("/project/tasks/{id}")
    public String tasks(@AuthenticationPrincipal User user,
                        @PathVariable Long id,
                        Model model) {
        Optional<Project> projectCandidate = projectService.findById(id);
        if (projectCandidate.isPresent() && isMemberOfProject(user, projectCandidate.get())) {
            model.addAttribute("project", projectCandidate.get());
            return "projectTasks";
        } else {
            return "redirect:/profile/" + user.getUsername();
        }
    }

    @GetMapping("/project/settings/{id}")
    public String settings(@AuthenticationPrincipal User user,
                           @PathVariable Long id,
                           Model model) {
        model.addAttribute("user", user);
//        if (user.getGithubToken() != null) {
//            List<RepositoryGithubModel> repos = gitHubService.getGitHubApi().getRepos(user);
//            model.addAttribute("repos", repos);
//        }

        Optional<Project> projectCandidate = projectService.findById(id);
        if (projectCandidate.isPresent() && isMemberOfProject(user, projectCandidate.get())) {
            model.addAttribute("project", projectCandidate.get());
//            if (user.getGithubToken() == null) {
//                model.addAttribute("authLink", gitHubService.getGitHubApi()
//                        .getAuthLink(
//                                GitHubScope.getFullAccess()
//                        )
//                );
//            }
            return "projectSettings";
        } else {
            return "redirect:/profile/" + user.getUsername();
        }
    }

    @GetMapping("/project/members/{id}")
    public String members(@AuthenticationPrincipal User user,
                          @PathVariable Long id,
                          Model model) {
        Optional<Project> projectCandidate = projectService.findById(id);
        if (projectCandidate.isPresent() && isMemberOfProject(user, projectCandidate.get())) {
            Project project = projectCandidate.get();
            model.addAttribute("project", project);
            if (project.getUsers() != null) {
                model.addAttribute("members", project.getUsers());
            }
            return "projectMembers";
        } else {
            return "redirect:/profile/" + user.getUsername();
        }
    }

    @PostMapping("/project/{id}/settings/addMember")
    public String addMember(@PathVariable Long id,
                            @RequestParam("username") String username,
                            ModelMap modelMap) {
        Optional<Project> projectCandidate = projectService.findById(id);
        if (projectCandidate.isPresent() && !projectService.addMember(projectCandidate.get(), username)) {
            Project project = projectCandidate.get();

            modelMap.addAttribute("error", "User " + username + " not found");
            modelMap.addAttribute("project", project);
            modelMap.addAttribute("members", project.getUsers());
            return "projectMembers";
        }
        return "redirect:/project/members/" + id;
    }

    @GetMapping(value = "/show_like_users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<UserDto> showLikeUsers(@RequestParam String username) {
        return projectService.getUsersLike(username).getUserDtoList();
    }

    private boolean isMemberOfProject(User user, Project project) {
        User userFromDb = userService.getUserById(user.getId()).get();
        return userFromDb.getProjects().contains(project);
    }
}
