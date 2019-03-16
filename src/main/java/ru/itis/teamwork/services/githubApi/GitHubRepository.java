package ru.itis.teamwork.services.githubApi;

import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.modelgit.RepositoryContent;
import ru.itis.teamwork.services.modelgit.RepositoryGithub;

import java.util.List;

public interface GitHubRepository {

    List<RepositoryGithub> getRepos(User user);
    int createRepo(User user, RepositoryGithub repository);
    int deleteRepo(User user, String repoName);
    List<RepositoryContent> getRepositoryContent(User user, String repoName);
}
