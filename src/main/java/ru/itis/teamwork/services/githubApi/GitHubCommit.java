package ru.itis.teamwork.services.githubApi;

import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.modelgit.Commit;

import java.util.List;

public interface GitHubCommit {

    List<Commit> getCommitsByRepoName(User user, String repoName);
}
