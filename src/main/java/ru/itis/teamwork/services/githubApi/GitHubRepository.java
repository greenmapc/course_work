package ru.itis.teamwork.services.githubApi;

import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.modelgit.RepositoryContentModel;
import ru.itis.teamwork.services.modelgit.RepositoryGithubModel;

import java.util.List;

public interface GitHubRepository {

    List<RepositoryGithubModel> getRepos(User user);
    int createRepo(User user, RepositoryGithubModel repository);
    int deleteRepo(User user, String repoName);
    List<RepositoryContentModel> getRepositoryContent(User user, String repoName);
}
