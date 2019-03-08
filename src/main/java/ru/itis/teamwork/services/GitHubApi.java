package ru.itis.teamwork.services;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.itis.teamwork.services.modelgit.Owner;
import ru.itis.teamwork.services.modelgit.RepositoryGithub;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.modelgit.Author;
import ru.itis.teamwork.services.modelgit.Commit;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class GitHubApi {

//   не убирать пока полностью не готово!!!!
    private String CLIENT_ID = "b8e1772648afd427f8cc";
    private String CLIENT_SECRET= "df0f334bc2c424539e911af3b6be9f8a11f8ecc3";
    private String GITHUB_API_AUTH ="https://github.com/login/oauth";
    private String REDIRECT;
    private String GITHUB = "https://api.github.com";
    private String FULL_ACCESS = "repo, gist, admin, user, delete_repo, write:discussion, admin:gpg_key";
    private HttpClient httpClient = HttpClients.createDefault();

    @SneakyThrows
    public String getAuthLink() {
        URIBuilder uriBuilder = new URIBuilder(GITHUB_API_AUTH.concat(Operation.AUTHORIZE.operation));
        uriBuilder.addParameter(AuthParam.client_id.param, CLIENT_ID)
                .addParameter(AuthParam.scope.param, FULL_ACCESS)
                .addParameter(AuthParam.redirect_uri.param, REDIRECT);

        return uriBuilder.build().toString();
    }

    @SneakyThrows
    public String getAccessToken(String code) {
        URIBuilder uriBuilder = new URIBuilder(GITHUB_API_AUTH.concat(Operation.ACCESS_TOKEN.operation));

        uriBuilder.setParameter(AuthParam.client_id.param, CLIENT_ID)
                .setParameter(AuthParam.client_secret.param, CLIENT_SECRET)
                .setParameter(AuthParam.redirect_uri.param, REDIRECT)
                .setParameter(AuthParam.code.param, code);

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
        JSONObject obj = this.getJsonResp(httpClient.execute(httpPost)).getJSONObject(0);
        System.out.println(obj);
        return obj.getString("access_token");
    }

    @SneakyThrows
    private JSONArray getJsonResp(HttpResponse response) {
        HttpEntity httpEntity = response.getEntity();
        String jsonRes = EntityUtils.toString(httpEntity);
        if (jsonRes.charAt(0) != '[') {
            jsonRes = ("[").concat(jsonRes).concat("]");
        }
        return new JSONArray(jsonRes);
    }

    @SneakyThrows
    public String getUserEmail(String token) {
        HttpGet httpGet = requestGet(
                GITHUB.
                        concat(Source.user.source).
                        concat("/").
                        concat("emails"),
                token);
        JSONObject obj = this.getJsonResp(httpClient.execute(httpGet)).getJSONObject(0);
        return obj.getString("email");
    }

    @SneakyThrows
    private HttpGet requestGet(String uri, String token) {
        URIBuilder uriBuilder = new URIBuilder(uri);
        System.out.println(uriBuilder.getPath());
        System.out.println(token);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader(HttpHeaders.AUTHORIZATION, "token ".concat(token));
        return httpGet;
    }

    @SneakyThrows
    public String getUsername(String token) {
        HttpGet httpGet = requestGet(GITHUB.concat(Source.user.source), token);
        JSONObject obj = this.getJsonResp(httpClient.execute(httpGet)).getJSONObject(0);
        System.out.println(obj.toString());
        return obj.getString("login");
    }

    @SneakyThrows
    public List<Commit> getCommitsByRepoName(User user, String repoName) {
        HttpGet httpGet = requestGet(
                GITHUB
                        .concat(Source.repos.source)
                        .concat("/")
                        .concat(user.getGitName())
                        .concat("/")
                        .concat(repoName)
                        .concat(Operation.COMMITS.operation),
                user.getGithubToken()
        );
        System.out.println(httpGet.getURI());
        JSONArray jsonArray = this.getJsonResp(httpClient.execute(httpGet));
        List<Commit> commits = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            commits.add(getCommit(jsonArray.getJSONObject(i)));
        }
        return commits;
    }

    private Commit getCommit(JSONObject jsonCommit) {
        System.out.println(jsonCommit);
        Commit commit = Commit.builder()
                .sha(jsonCommit.getString("sha"))
                .author(
                        Author.builder()
                                .email(jsonCommit.getJSONObject("commit").getJSONObject("author").getString("email"))
                                .name(jsonCommit.getJSONObject("commit").getJSONObject("author").getString("name"))
                                .build()
                )
                .htmlUrl(jsonCommit.getString("html_url"))
                .message(jsonCommit.getJSONObject("commit").getString("message"))
                .build();
        return commit;
    }

    @SneakyThrows
    public List<RepositoryGithub> getRepos(User user){
        HttpGet httpGet = requestGet(
                GITHUB
                        .concat(Source.user.source)
                        .concat(Source.repos.source),
                user.getGithubToken()
        );
        JSONArray reposJson = this.getJsonResp(this.httpClient.execute(httpGet));

        List<RepositoryGithub> repositoryGithubs = new ArrayList<>();
        for (int i=0; i<reposJson.length(); i++){
            repositoryGithubs.add(this.getRepo(
                    reposJson.getJSONObject(i)
            ));
        }

        return repositoryGithubs;
    }

    private RepositoryGithub getRepo(JSONObject repoJson){
        JSONObject ownerJson = repoJson.getJSONObject("owner");

        RepositoryGithub repositoryGithub = RepositoryGithub.builder()
                .id(repoJson.getLong("id"))
                .name(repoJson.getString("name"))
                .owner(Owner.builder()
                        .login(ownerJson.getString("login"))
                        .avatarUrl(ownerJson.getString("avatar_url"))
                        .url(ownerJson.getString("url"))
                        .build())
                .htmlUrl(repoJson.getString("html_url"))
                .description( repoJson.get("description") == JSONObject.NULL ? null : repoJson.getString("description"))
                .language(repoJson.getString("language"))
                .isPrivate(repoJson.getBoolean("private"))
                .build();

        return repositoryGithub;
    }

    private enum Operation {
        AUTHORIZE("/authorize"),
        ACCESS_TOKEN("/access_token"),
        COMMITS("/commits");

        private String operation;

        Operation(String operation) {
            this.operation = operation;
        }
    }

    private enum AuthParam {
        client_id("client_id"),
        client_secret("client_secret"),
        redirect_uri("redirect_uri"),
        scope("scope"),
        code("code");

        private String param;

        AuthParam(String param) {
            this.param = param;
        }
    }

    private enum Source {
        user("/user"),
        repos("/repos");

        private String source;

        Source(String source) {
            this.source = source;
        }
    }

    public static void main(String[] args) {
        GitHubApi gitHubApi = new GitHubApi();
        User user = new User();
        user.setGithubToken("e38147db56efa0d52cb3f6b3c3579ccfbc661eb0");
        user.setGitName("daniszam");
//        List<Commit> commitList = gitHubApi.getCommitsByRepoName(user, "bankservice");
        List<RepositoryGithub> repositoryGithubs = gitHubApi.getRepos(user);
        System.out.println(repositoryGithubs);
    }
}
