package ru.itis.teamwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.util.githubApi.GitHubApi;
import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

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

    public RepositoryGithubModel getRepositoryWithBranches(User user, String repoName) {
        RepositoryGithubModel repositoryGithubModel = gitHubApi.getRepoByName(user, repoName);
        if (repositoryGithubModel != null) {
            repositoryGithubModel.setBranches(gitHubApi.getBranches(user, repositoryGithubModel));
            repositoryGithubModel.getBranches().forEach(
                    a -> a.setDownloadLink(
                            gitHubApi.getBranchRepositoryDownloadLink(user, repositoryGithubModel, a)
                    )
            );
        }
        return repositoryGithubModel;
    }
}
