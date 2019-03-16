package ru.itis.teamwork.services.githubApi;

import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.modelgit.Comment;
import ru.itis.teamwork.services.modelgit.Commit;
import ru.itis.teamwork.services.modelgit.RepositoryGithub;

import java.util.List;

public interface GitHubComment {

    int addCommentToCommit(User user,
                           RepositoryGithub repositoryGithub,
                           Commit commit,
                           Comment comment);

    List<Comment> getCommentsByRepo(User user,
                                    RepositoryGithub repositoryGithub);


    int deleteComment(User user,
                      RepositoryGithub repositoryGithub,
                      Comment comment);
}

