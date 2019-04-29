package ru.itis.teamwork.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.GitHubService;
import ru.itis.teamwork.services.ProjectService;
import ru.itis.teamwork.services.UserService;
import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

import java.util.List;

@Controller
public class GitHubController {
    private final GitHubService gitHubService;

    private final UserService userService;

    private final ProjectService projectService;

    public GitHubController(GitHubService gitHubService,
                            UserService userService,
                            ProjectService projectService) {
        this.gitHubService = gitHubService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping("/gitCode")
    public String redirectToProfile(@RequestParam("code") String code,
                                    @AuthenticationPrincipal User user) {
        String accessToken = gitHubService.getGitHubApi().getAccessToken(code);
        String gitName = gitHubService.getGitHubApi().getUsername(accessToken);
        User userFromDb = userService.getUserById(user.getId()).get();
        userFromDb.setGithubToken(accessToken);
        userFromDb.setGitName(gitName);
        userService.saveUser(userFromDb);
        return "redirect:/profile";
    }

    @PostMapping("/project/weavingRepo")
    public String weavingRepo(@RequestParam("projectId") Project project,
                              @RequestParam("repos") String repoName,
                              @AuthenticationPrincipal User user,
                              Model model) {
        List<RepositoryGithubModel> modelList = gitHubService.getGitHubApi().getRepos(user);
        RepositoryGithubModel repo = modelList.stream()
                .filter(a -> a.getName().equals(repoName))
                .findAny().orElse(null);
        if (repo != null) {
            project.setGitLink(repo.getHtmlUrl());
            project.setGitRepositoryName(repo.getName());
            projectService.update(project);
        }
        return "redirect:/project/settings/" + project.getId();
    }

}
