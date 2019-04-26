package ru.itis.teamwork.util.githubApi;

import com.fasterxml.jackson.contrib.jsonpath.DefaultJsonUnmarshaller;
import com.fasterxml.jackson.contrib.jsonpath.JsonUnmarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.util.modelgit.Comment;
import ru.itis.teamwork.util.modelgit.Commit;
import ru.itis.teamwork.util.modelgit.RepositoryContentModel;
import ru.itis.teamwork.util.modelgit.RepositoryGithubModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GitHubApi {

    private GitHubCommit githubCommit;
    private GitHubRepository gitHubRepository;
    private GitHubComment gitHubComment;

    public GitHubApi(){
        this.githubCommit = new GitHubApiCommitImpl(jsonUnmarshaller, httpClient);
        this.gitHubRepository = new GitHubApiRepositoryImpl(jsonUnmarshaller, httpClient);
        this.gitHubComment = new GitHubApiCommentImpl(jsonUnmarshaller, httpClient);
    }

    //   не убирать пока полностью не готово!!!!
    private String CLIENT_ID = "b8e1772648afd427f8cc";
    private String CLIENT_SECRET = "df0f334bc2c424539e911af3b6be9f8a11f8ecc3";
    private String GITHUB_API_AUTH = "https://github.com/login/oauth";
    private String REDIRECT;
    protected static String GITHUB = "https://api.github.com";
    private HttpClient httpClient = HttpClients.createDefault();
    private JsonUnmarshaller jsonUnmarshaller = new DefaultJsonUnmarshaller();
    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public String getAuthLink(Set<GitHubScope> scopes) {

        String strScopes = scopes.stream().map(GitHubScope::getScope).collect(Collectors.joining(","));

        URIBuilder uriBuilder = new URIBuilder(GITHUB_API_AUTH.concat(GitHubOperation.AUTHORIZE.operation));
        uriBuilder.addParameter(GitHubAuthParam.CLIENT_ID.param, CLIENT_ID)
                .addParameter(GitHubAuthParam.SCOPE.param, strScopes)
                .addParameter(GitHubAuthParam.REDIRECT_URI.param, REDIRECT);

        return uriBuilder.build().toString();
    }

    @SneakyThrows
    public String getAccessToken(String code) {
        URIBuilder uriBuilder = new URIBuilder(GITHUB_API_AUTH.concat(GitHubOperation.ACCESS_TOKEN.operation));

        uriBuilder.setParameter(GitHubAuthParam.CLIENT_ID.param, CLIENT_ID)
                .setParameter(GitHubAuthParam.CLIENT_SECRET.param, CLIENT_SECRET)
                .setParameter(GitHubAuthParam.REDIRECT_URI.param, REDIRECT)
                .setParameter(GitHubAuthParam.CODE.param, code);

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
        JSONObject obj = getJsonResp(httpClient.execute(httpPost)).getJSONObject(0);
        System.out.println(obj);
        return obj.getString("access_token");
    }

    @SneakyThrows
    public static JSONArray getJsonResp(HttpResponse response) {
        HttpEntity httpEntity = response.getEntity();
        String jsonRes = EntityUtils.toString(httpEntity);
        if (jsonRes != null) {
            if (jsonRes.charAt(0) != '[') {
                jsonRes = ("[").concat(jsonRes).concat("]");
            }
        }
        return new JSONArray(jsonRes);
    }

    @SneakyThrows
    public String getUserEmail(String token) {
        HttpGet httpGet = getGetRequest(
                GITHUB.
                        concat(GitHubSource.USER.source).
                        concat(GitHubOperation.EMAILS.operation),
                token);
        JSONObject obj = getJsonResp(httpClient.execute(httpGet)).getJSONObject(0);
        return obj.getString("email");
    }

    @SneakyThrows
    static HttpGet getGetRequest(String uri, String token) {
        URIBuilder uriBuilder = new URIBuilder(uri);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader(HttpHeaders.AUTHORIZATION, "token ".concat(token));
        return httpGet;
    }

    @SneakyThrows
    public String getUsername(String token) {
        HttpGet httpGet = getGetRequest(GITHUB.concat(GitHubSource.USER.source), token);
        JSONObject obj = getJsonResp(httpClient.execute(httpGet)).getJSONObject(0);
        System.out.println(obj.toString());
        return obj.getString("login");
    }

    @SneakyThrows
    public List<Commit> getCommitsByRepoName(User user, String repoName) {
        return this.githubCommit.getCommitsByRepoName(user, repoName);
    }


    @SneakyThrows
    public List<RepositoryGithubModel> getRepos(User user) {
        return this.gitHubRepository.getRepos(user);
    }


    @SneakyThrows
    public int createRepo(User user, RepositoryGithubModel repository) {
        return this.gitHubRepository.createRepo(user, repository);
    }

    @SneakyThrows
    public int deleteRepo(User user, String repoName) {
        return this.gitHubRepository.deleteRepo(user, repoName);
    }

    @SneakyThrows
    public List<RepositoryContentModel> getRepositoryContent(User user, String repoName) {
        return this.gitHubRepository.getRepositoryContent(user, repoName);
    }

    @SneakyThrows
    static HttpPost getJsonPostRequest(URI uri, Object jsonObject, String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(jsonObject);

        HttpPost post = new HttpPost(uri);
        StringEntity entity = new StringEntity(json);
        post.setEntity(entity);

        post.setHeader("Content-type", "application/json");
        post.setHeader(HttpHeaders.AUTHORIZATION, "token ".concat(token));
        return post;
    }

    @SneakyThrows
    public int addCommentToCommit(User user,
                                  RepositoryGithubModel repositoryGithubModel,
                                  Commit commit,
                                  Comment comment) {

        return this.gitHubComment.addCommentToCommit(user, repositoryGithubModel, commit, comment);
    }

    @SneakyThrows
    public List<Comment> getCommentsByRepo(User user,
                                           RepositoryGithubModel repositoryGithubModel) {


        return this.gitHubComment.getCommentsByRepo(user, repositoryGithubModel);
    }

    @SneakyThrows
    public int deleteComment(User user,
                             RepositoryGithubModel repositoryGithubModel,
                             Comment comment){

        return this.gitHubComment.deleteComment(user, repositoryGithubModel, comment);
    }


    public static void main(String[] args) throws URISyntaxException {
        GitHubApi gitHubApi = new GitHubApi();
        User user = new User();

        System.out.println(gitHubApi.getAuthLink(GitHubScope.getFullAccess()));


//        System.out.println(gitHubApi.getAccessToken("0ba39ca7cefcf858db7e"));

        user.setGithubToken("");
        user.setGitName("alxarsnk");
        List<RepositoryGithubModel> repositoryGithub = gitHubApi.getRepos(user);


        System.out.println(repositoryGithub);
//        List<Commit> commits = gitHubApi.getCommitsByRepoName(user, repositoryGithub.get(0).getName());
//        System.out.println(commits.get(0));
        Comment comment = Comment.builder()
                .body("from1 github api")
                .path("src/main/java/utils/Circle.java")
                .position(10)
                .build();

//
//        int statusCode = gitHubApi.addCommentToCommit(
//                user,
//                repositoryGithub.get(0),
//                commits.get(0),
//                comment);
//        List<Comment> comments = gitHubApi.getCommentsByRepo(user, repositoryGithub.get(0));
//        System.out.println(comments);
//        System.out.println(gitHubApi.deleteComment(user, repositoryGithub.get(0), comments.get(0)));
         gitHubApi.createRepo(user, RepositoryGithubModel.builder().name("Sanya lox").build());
//          gitHubApi.deleteRepo(user, "First1-api-rep");
//        System.out.println(gitHubApi.deleteRepo(user, "First-api-rep"));
//        List<Commit> commitList = gitHubApi.getCommitsByRepoName(USER, "bankservice");
//        List<RepositoryGithubModel> repositoryGithubs = gitHubApi.getRepos(USER);
//        System.out.println(repositoryGithubs);

    }
}
