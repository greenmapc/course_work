package ru.itis.teamwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.util.githubApi.GitHubApi;

@Service
public class GitHubService {
    private final GitHubApi gitHubApi;

    @Autowired
    public GitHubService(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public GitHubApi getGitHubApi() {
        return gitHubApi;
    }
}
