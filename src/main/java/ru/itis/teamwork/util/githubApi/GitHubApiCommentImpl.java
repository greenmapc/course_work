package ru.itis.teamwork.util.githubApi;

import com.fasterxml.jackson.contrib.jsonpath.JsonUnmarshaller;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.util.modelgit.Comment;
import ru.itis.teamwork.util.modelgit.Commit;
import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

import java.util.ArrayList;
import java.util.List;

public class GitHubApiCommentImpl implements GitHubComment {

    private JsonUnmarshaller jsonUnmarshaller;
    private HttpClient httpClient;

    public GitHubApiCommentImpl(JsonUnmarshaller jsonUnmarshaller, HttpClient httpClient){
        this.jsonUnmarshaller = jsonUnmarshaller;
        this.httpClient = httpClient;
    }


    @SneakyThrows
    public int addCommentToCommit(User user,
                                  RepositoryGithubModel repositoryGithubModel,
                                  Commit commit,
                                  Comment comment) {

        URIBuilder uriBuilder = new URIBuilder(GitHubApi.GITHUB
                .concat(GitHubSource.REPOS.source)
                .concat("/")
                .concat(user.getGitName())
                .concat("/")
                .concat(repositoryGithubModel.getName())
                .concat(GitHubSource.COMMITS.source)
                .concat("/")
                .concat(commit.getSha())
                .concat(GitHubSource.COMMENTS.source));

        System.out.println(uriBuilder.build());

        HttpPost post = GitHubApi.getJsonPostRequest(
                uriBuilder.build(),
                comment,
                user.getGithubToken());

        HttpResponse response = httpClient.execute(post);

        return response.getStatusLine().getStatusCode();
    }

    @SneakyThrows
    public List<Comment> getCommentsByRepo(User user,
                                           RepositoryGithubModel repositoryGithubModel) {

        HttpGet get = GitHubApi.getGetRequest(
                GitHubApi.GITHUB
                        .concat(GitHubSource.REPOS.source)
                        .concat("/")
                        .concat(user.getGitName())
                        .concat("/")
                        .concat(repositoryGithubModel.getName())
                        .concat(GitHubSource.COMMENTS.source),
                user.getGithubToken());

        System.out.println(get.getURI());

        JSONArray jsonComments = GitHubApi.getJsonResp(this.httpClient.execute(get));
        List<Comment> comments = new ArrayList<>();

        for( int i = 0; i< jsonComments.length(); i++){
            comments.add(this.getComment(jsonComments.getJSONObject(i)));
        }

        return comments;
    }

    @SneakyThrows
    private Comment getComment(JSONObject jsonComment){
        return this.jsonUnmarshaller.unmarshal(Comment.class, jsonComment.toString());
    }

    @SneakyThrows
    public int deleteComment(User user,
                             RepositoryGithubModel repositoryGithubModel,
                             Comment comment){

        URIBuilder uriBuilder = new URIBuilder(GitHubApi.GITHUB
                .concat(GitHubSource.REPOS.source)
                .concat("/")
                .concat(user.getGitName())
                .concat("/")
                .concat(repositoryGithubModel.getName())
                .concat(GitHubSource.COMMENTS.source)
                .concat("/")
                .concat(String.valueOf(comment.getCommentId())));

        HttpDelete delete = new HttpDelete(uriBuilder.build());
        delete.setHeader(HttpHeaders.AUTHORIZATION, "token ".concat(user.getGithubToken()));

        HttpResponse response = this.httpClient.execute(delete);

        return response.getStatusLine().getStatusCode();
    }
}
