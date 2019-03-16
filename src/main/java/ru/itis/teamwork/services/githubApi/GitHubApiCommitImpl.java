package ru.itis.teamwork.services.githubApi;

import com.fasterxml.jackson.contrib.jsonpath.JsonUnmarshaller;
import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.modelgit.Commit;

import java.util.ArrayList;
import java.util.List;

class GitHubApiCommitImpl implements GitHubCommit {

    private JsonUnmarshaller jsonUnmarshaller;
    private HttpClient httpClient;

    public GitHubApiCommitImpl(JsonUnmarshaller jsonUnmarshaller, HttpClient httpClient){
        this.jsonUnmarshaller = jsonUnmarshaller;
        this.httpClient = httpClient;
    }

    @SneakyThrows
    public List<Commit> getCommitsByRepoName(User user, String repoName) {
        HttpGet httpGet = GitHubApi.getGetRequest(
                GitHubApi.GITHUB
                        .concat(GitHubSource.REPOS.source)
                        .concat("/")
                        .concat(user.getGitName())
                        .concat("/")
                        .concat(repoName)
                        .concat(GitHubOperation.COMMITS.operation),
                user.getGithubToken()
        );

        JSONArray jsonArray = GitHubApi.getJsonResp(httpClient.execute(httpGet));

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
}
