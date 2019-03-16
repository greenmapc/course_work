package ru.itis.teamwork.util.githubApi;

import ru.itis.teamwork.models.User;
import ru.itis.teamwork.util.modelgit.Comment;
import ru.itis.teamwork.util.modelgit.Commit;
import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

import java.util.List;

public interface GitHubComment {

    int addCommentToCommit(User user,
                           RepositoryGithubModel repositoryGithubModel,
                           Commit commit,
                           Comment comment);

    List<Comment> getCommentsByRepo(User user,
                                    RepositoryGithubModel repositoryGithubModel);


    int deleteComment(User user,
                      RepositoryGithubModel repositoryGithubModel,
                      Comment comment);
}

