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
import ru.itis.teamwork.util.modelgit.Owner;
import ru.itis.teamwork.util.modelgit.RepositoryContentModel;
import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

import java.util.ArrayList;
import java.util.List;

public class GitHubApiRepositoryImpl implements GitHubRepository {

    private JsonUnmarshaller jsonUnmarshaller;
    private HttpClient httpClient;

    public GitHubApiRepositoryImpl(JsonUnmarshaller jsonUnmarshaller, HttpClient httpClient) {
        this.jsonUnmarshaller = jsonUnmarshaller;
        this.httpClient = httpClient;
    }

    @SneakyThrows
    public List<RepositoryGithubModel> getRepos(User user) {
        HttpGet httpGet = GitHubApi.getGetRequest(
                GitHubApi.GITHUB
                        .concat(GitHubSource.USER.source)
                        .concat(GitHubSource.REPOS.source),
                user.getGithubToken()
        );

        JSONArray reposJson = GitHubApi.getJsonResp(this.httpClient.execute(httpGet));

        List<RepositoryGithubModel> repositoryGitHubModels = new ArrayList<>();
        for (int i = 0; i < reposJson.length(); i++) {

//          вопрос нужно ли сразу вытаскивать контент из репозитория???
            RepositoryGithubModel repositoryGithubModel = this.getRepo(reposJson.getJSONObject(i));
            List<RepositoryContentModel> repositoryContentModels = this.getRepositoryContent(user, repositoryGithubModel.getName());
            repositoryGithubModel.setRepositoryContentModelList(repositoryContentModels);

            repositoryGithubModel.setCollaborators(this.getCollaborators(
                    user,
                    reposJson.getJSONObject(i)
                            .getString("collaborators_url").replace("{/collaborator}", "")
                    )
            );

            repositoryGitHubModels.add(repositoryGithubModel);
        }

        return repositoryGitHubModels;
    }

    @SneakyThrows
    private RepositoryGithubModel getRepo(JSONObject repoJson) {

        RepositoryGithubModel repositoryGithubModel = jsonUnmarshaller.
                unmarshal(RepositoryGithubModel.class, repoJson.toString());

        return repositoryGithubModel;
    }

    @SneakyThrows
    private List<Owner> getCollaborators(User user, String uri) {
        HttpGet get = GitHubApi.getGetRequest(uri, user.getGithubToken());

        JSONArray collaboratorsJson = GitHubApi.getJsonResp(this.httpClient.execute(get));

        List<Owner> collaborators = new ArrayList<>();
        for (int i = 0; i < collaboratorsJson.length(); i++) {
            collaborators.add(this.getOwner(collaboratorsJson.getJSONObject(i)));
        }

        return collaborators;
    }

    @SneakyThrows
    private Owner getOwner(JSONObject ownerJson) {
        Owner owner = jsonUnmarshaller.
                unmarshal(Owner.class, ownerJson.toString());

        return owner;

    }

    @SneakyThrows
    public int createRepo(User user, RepositoryGithubModel repository) {
        URIBuilder uriBuilder = new URIBuilder(GitHubApi.GITHUB
                .concat(GitHubSource.USER.source)
                .concat(GitHubSource.REPOS.source));


        HttpPost post = GitHubApi.getJsonPostRequest(
                uriBuilder.build(),
                repository,
                user.getGithubToken());

        HttpResponse response = httpClient.execute(post);
        return response.getStatusLine().getStatusCode();
    }

    @SneakyThrows
    public int deleteRepo(User user, String repoName) {
        URIBuilder uriBuilder = new URIBuilder(GitHubApi.GITHUB
                .concat(GitHubSource.REPOS.source)
                .concat("/")
                .concat(user.getGitName())
                .concat("/")
                .concat(repoName));

        HttpDelete delete = new HttpDelete(uriBuilder.build());
        delete.setHeader(HttpHeaders.AUTHORIZATION, "token ".concat(user.getGithubToken()));
        HttpResponse response = httpClient.execute(delete);
        return response.getStatusLine().getStatusCode();
    }

    @SneakyThrows
    public List<RepositoryContentModel> getRepositoryContent(User user, String repoName) {
        HttpGet httpGet = GitHubApi.getGetRequest(
                GitHubApi.GITHUB
                        .concat(GitHubSource.REPOS.source)
                        .concat("/")
                        .concat(user.getGitName())
                        .concat(repoName)
                        .concat(GitHubSource.CONTENTS.source),
                user.getGithubToken());

        JSONArray contentArray = GitHubApi.getJsonResp(this.httpClient.execute(httpGet));

        List<RepositoryContentModel> repositoryContentModels = new ArrayList<>();
        for (int i = 0; i < contentArray.length(); i++) {
            repositoryContentModels.add(this.getContentFileDescription(
                    contentArray.getJSONObject(i)
            ));
        }
        return repositoryContentModels;
    }

    @SneakyThrows
    private RepositoryContentModel getContentFileDescription(JSONObject content) {
        RepositoryContentModel repositoryContentModel = jsonUnmarshaller.unmarshal(
                RepositoryContentModel.class,
                content.toString()
        );
        return repositoryContentModel;
    }
}
