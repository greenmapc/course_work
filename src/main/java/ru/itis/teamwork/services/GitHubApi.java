package ru.itis.teamwork.services;

import com.fasterxml.jackson.contrib.jsonpath.DefaultJsonUnmarshaller;
import com.fasterxml.jackson.contrib.jsonpath.JsonUnmarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.Map;

@Component
@Getter
@Setter
public class GitHubApi {

    //   не убирать пока полностью не готово!!!!
    private String CLIENT_ID = "b8e1772648afd427f8cc";
    private String CLIENT_SECRET = "df0f334bc2c424539e911af3b6be9f8a11f8ecc3";
    private String GITHUB_API_AUTH = "https://github.com/login/oauth";
    private String REDIRECT;
    private String GITHUB = "https://api.github.com";
    private String FULL_ACCESS = "repo,gist,user,delete_repo,write,admin:org,admin:public_key," +
            "admin:org_hook,notifications,write:discussion,admin:gpg_key";
    private HttpClient httpClient = HttpClients.createDefault();
    private JsonUnmarshaller jsonUnmarshaller = new DefaultJsonUnmarshaller();

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
                        concat(Operation.EMAILS.operation),
                token);
        JSONObject obj = this.getJsonResp(httpClient.execute(httpGet)).getJSONObject(0);
        return obj.getString("email");
    }

    @SneakyThrows
    private HttpGet requestGet(String uri, String token) {
        URIBuilder uriBuilder = new URIBuilder(uri);
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
        JSONArray jsonArray = this.getJsonResp(httpClient.execute(httpGet));
        List<Commit> commits = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            commits.add(getCommit(jsonArray.getJSONObject(i)));
        }
        return commits;
    }

    @SneakyThrows
    private Commit getCommit(JSONObject jsonCommit) {

        Commit commit = jsonUnmarshaller.unmarshal(
                Commit.class,
                jsonCommit.toString()
        );
        return commit;
    }

    @SneakyThrows
    public List<RepositoryGithub> getRepos(User user) {
        HttpGet httpGet = requestGet(
                GITHUB
                        .concat(Source.user.source)
                        .concat(Source.repos.source),
                user.getGithubToken()
        );

        JSONArray reposJson = this.getJsonResp(this.httpClient.execute(httpGet));

        List<RepositoryGithub> repositoryGithubs = new ArrayList<>();
        for (int i = 0; i < reposJson.length(); i++) {
            repositoryGithubs.add(this.getRepo(
                    reposJson.getJSONObject(i)
            ));
        }

        return repositoryGithubs;
    }

    @SneakyThrows
    private RepositoryGithub getRepo(JSONObject repoJson) {

        RepositoryGithub repositoryGithub = jsonUnmarshaller.
                unmarshal(RepositoryGithub.class, repoJson.toString());

        return repositoryGithub;
    }

    @SneakyThrows
    public int createRepo(User user, RepositoryGithub repository) {
        URIBuilder uriBuilder = new URIBuilder(GITHUB
                .concat(Source.user.source)
                .concat(Source.repos.source));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(repository);

        HttpPost post = new HttpPost(uriBuilder.build());

        StringEntity entity = new StringEntity(json);
        post.setEntity(entity);

        post.setHeader("Content-type", "application/json");
        post.setHeader(HttpHeaders.AUTHORIZATION, "token ".concat(user.getGithubToken()));
        HttpResponse response = httpClient.execute(post);
        return response.getStatusLine().getStatusCode();
    }

    @SneakyThrows
    public int deleteRepo(User user, String repoName){
        URIBuilder uriBuilder = new URIBuilder(GITHUB
                .concat(Source.repos.source)
                .concat("/")
                .concat(user.getGitName())
                .concat("/")
                .concat(repoName));

        HttpDelete delete = new HttpDelete(uriBuilder.build());
        delete.setHeader(HttpHeaders.AUTHORIZATION, "token ".concat(user.getGithubToken()));
        HttpResponse response = httpClient.execute(delete);
        return response.getStatusLine().getStatusCode();
    }

    private enum Operation {
        AUTHORIZE("/authorize"),
        ACCESS_TOKEN("/access_token"),
        COMMITS("/commits"),
        EMAILS("/emails");

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
        System.out.println(gitHubApi.getAuthLink());
//        System.out.println(gitHubApi.getAccessToken("6fece3d7c5bdd6f62529"));

        user.setGithubToken(" ");
        user.setGitName("daniszam");
//        gitHubApi.createRepo(user, RepositoryGithub.builder().name("First1 api rep").build());
        System.out.println(gitHubApi.deleteRepo(user, "First-api-rep"));
//        List<Commit> commitList = gitHubApi.getCommitsByRepoName(user, "bankservice");
//        List<RepositoryGithub> repositoryGithubs = gitHubApi.getRepos(user);
//        System.out.println(repositoryGithubs);

    }
}
