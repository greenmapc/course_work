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
import ru.itis.teamwork.util.modelgit.Branch;
import ru.itis.teamwork.util.modelgit.Owner;
import ru.itis.teamwork.util.modelgit.RepositoryContentModel;
import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

import java.io.IOException;
import java.net.URISyntaxException;
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
                GitHubApi.GITHUB_API
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

    @Override
    public RepositoryGithubModel getRepoByName(User user, String name) {
        HttpGet httpGet = GitHubApi.getGetRequest(
                GitHubApi.GITHUB_API
                        .concat(GitHubSource.REPOS.source)
                        .concat("/")
                        .concat(user.getGitName())
                        .concat("/").concat(name),
                user.getGithubToken()
        );
        try {
            JSONArray repoJson = GitHubApi.getJsonResp(this.httpClient.execute(httpGet));
            if (repoJson.length() > 0){
                return getRepo(repoJson.getJSONObject(0));
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private RepositoryGithubModel getRepo(JSONObject repoJson) {

        RepositoryGithubModel repositoryGithubModel = null;
        try {
            repositoryGithubModel = jsonUnmarshaller.
                    unmarshal(RepositoryGithubModel.class, repoJson.toString());
        } catch (IOException e) {
            return null;
        }

        return repositoryGithubModel;
    }

    private List<Owner> getCollaborators(User user, String uri) {
        HttpGet get = GitHubApi.getGetRequest(uri, user.getGithubToken());

        JSONArray collaboratorsJson = null;
        try {
            collaboratorsJson = GitHubApi.getJsonResp(this.httpClient.execute(get));
        } catch (IOException e) {
            return null;
        }

        List<Owner> collaborators = new ArrayList<>();
        for (int i = 0; i < collaboratorsJson.length(); i++) {
            collaborators.add(this.getOwner(collaboratorsJson.getJSONObject(i)));
        }

        return collaborators;
    }

    private Owner getOwner(JSONObject ownerJson) {
        Owner owner = null;
        try {
            owner = jsonUnmarshaller.
                    unmarshal(Owner.class, ownerJson.toString());
        } catch (IOException e) {
            return null;
        }

        return owner;

    }



    public int createRepo(User user, RepositoryGithubModel repository) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(GitHubApi.GITHUB_API
                    .concat(GitHubSource.USER.source)
                    .concat(GitHubSource.REPOS.source));
            HttpPost post = GitHubApi.getJsonPostRequest(
                    uriBuilder.build(),
                    repository,
                    user.getGithubToken());
            HttpResponse response = httpClient.execute(post);
            return response.getStatusLine().getStatusCode();
        } catch (URISyntaxException | IOException e) {
           return 500;
        }

    }

    public int deleteRepo(User user, String repoName) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(GitHubApi.GITHUB_API
                    .concat(GitHubSource.REPOS.source)
                    .concat("/")
                    .concat(user.getGitName())
                    .concat("/")
                    .concat(repoName));
            HttpDelete delete = new HttpDelete(uriBuilder.build());
            delete.setHeader(HttpHeaders.AUTHORIZATION, "token ".concat(user.getGithubToken()));
            HttpResponse response = httpClient.execute(delete);
            return response.getStatusLine().getStatusCode();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return 500;
    }

    @Override
    public List<Branch> getBranches(User user, RepositoryGithubModel repository) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(GitHubApi.GITHUB_API
                    .concat(GitHubSource.REPOS.source)
                    .concat("/")
                    .concat(user.getGitName())
                    .concat("/")
                    .concat(repository.getName())
                    .concat(GitHubSource.BRANCHES.source));
            HttpGet get = new HttpGet(uriBuilder.build());
            get.setHeader(HttpHeaders.AUTHORIZATION, "token ".concat(user.getGithubToken()));
            HttpResponse response = httpClient.execute(get);
            JSONArray contentArray = GitHubApi.getJsonResp(response);
            List<Branch> branches = new ArrayList<>();
            for (int i = 0; i < contentArray.length(); i++) {
                branches.add(this.getBranch(
                        contentArray.getJSONObject(i)
                ));
            }
            return branches;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getDownloadLink(User user, RepositoryGithubModel repositoryGithubModel, Branch branch) {
        return GitHubApi.GITHUB
                .concat("/").concat(user.getGitName())
                .concat("/").concat(repositoryGithubModel.getName())
                .concat("/archive")
                .concat("/").concat(branch.getName()).concat(".zip");
    }

    @SneakyThrows
    public List<RepositoryContentModel> getRepositoryContent(User user, String repoName) {
        HttpGet httpGet = GitHubApi.getGetRequest(
                GitHubApi.GITHUB_API
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

    private Branch getBranch(JSONObject jsonObject){
        try {
            Branch branch = jsonUnmarshaller.unmarshal(Branch.class, jsonObject.toString());
            return branch;
        } catch (IOException e) {
            return null;
        }
    }
}
