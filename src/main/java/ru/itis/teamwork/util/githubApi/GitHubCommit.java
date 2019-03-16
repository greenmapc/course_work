package ru.itis.teamwork.util.githubApi;

import ru.itis.teamwork.models.User;
import ru.itis.teamwork.util.modelgit.Commit;

import java.util.List;

public interface GitHubCommit {

    List<Commit> getCommitsByRepoName(User user, String repoName);
}
