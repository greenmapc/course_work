package ru.itis.teamwork.util.githubApi;

import ru.itis.teamwork.models.User;
import ru.itis.teamwork.util.modelgit.Branch;
import ru.itis.teamwork.util.modelgit.RepositoryContentModel;
import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

import java.util.List;

public interface GitHubRepository {

    List<RepositoryGithubModel> getRepos(User user);
    RepositoryGithubModel getRepoByName(User user, String name);
    int createRepo(User user, RepositoryGithubModel repository);
    int deleteRepo(User user, String repoName);
    List<Branch> getBranches(User user, RepositoryGithubModel repository);
    String getDownloadLink(User user, RepositoryGithubModel repositoryGithubModel, Branch branch);
    List<RepositoryContentModel> getRepositoryContent(User user, String repoName);
}
