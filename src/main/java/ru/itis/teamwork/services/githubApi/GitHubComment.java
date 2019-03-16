package ru.itis.teamwork.services.githubApi;

import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.modelgit.Comment;
import ru.itis.teamwork.services.modelgit.Commit;
import ru.itis.teamwork.services.modelgit.RepositoryGithubModel;

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

