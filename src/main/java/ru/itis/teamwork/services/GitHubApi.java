package ru.itis.teamwork.utils;

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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@PropertySource("classpath:/git.properties")
public class GitHubApi {
    @Resource
    private Environment env;

    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String GITHUB_API_AUTH;
    private final String REDIRECT;
    private final String GITHUB;

    private HttpClient httpClient;

    public GitHubApi() {
        this.httpClient = HttpClients.createDefault();
        this.CLIENT_ID = env.getRequiredProperty("CLIENT_ID");
        this.CLIENT_SECRET = env.getRequiredProperty("CLIENT_SECRET");
        this.GITHUB_API_AUTH = env.getRequiredProperty("GITHUB_API_AUTH");
        this.REDIRECT = env.getRequiredProperty("REDIRECT");
        this.GITHUB = env.getRequiredProperty("GITHUB");
    }

    @SneakyThrows
    public String getAuthLink() {
        URIBuilder uriBuilder = new URIBuilder(GITHUB_API_AUTH.concat(Operation.AUTHORIZE.operation));
        uriBuilder.addParameter(Param.client_id.param, CLIENT_ID)
                .addParameter(Param.scope.param, "repo, gist, admin, user, delete_repo, write:discussion, admin:gpg_key")
                .addParameter(Param.redirect_uri.param, REDIRECT);

        return uriBuilder.build().toString();
    }

    @SneakyThrows
    public String getAccessToken(String code) {
        URIBuilder uriBuilder = new URIBuilder(GITHUB_API_AUTH.concat(Operation.ACCESS_TOKEN.operation));

        uriBuilder.setParameter(Param.client_id.param, CLIENT_ID)
                .setParameter(Param.client_secret.param, CLIENT_SECRET)
                .setParameter(Param.redirect_uri.param, REDIRECT)
                .setParameter(Param.code.param, code);

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
        JSONObject obj = this.getJsonResp(httpClient.execute(httpPost)).getJSONObject(0);

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
        URIBuilder uriBuilder = new URIBuilder(GITHUB.concat("emails"));
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader(HttpHeaders.AUTHORIZATION, "token ".concat(token));
        JSONObject obj = this.getJsonResp(httpClient.execute(httpGet)).getJSONObject(0);
        return obj.getString("email");
    }

    @SneakyThrows
    public String getUsername(String token) {
        URIBuilder uriBuilder = new URIBuilder(GITHUB);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader(HttpHeaders.AUTHORIZATION, "token ".concat(token));
        JSONObject obj = this.getJsonResp(httpClient.execute(httpGet)).getJSONObject(0);
        return obj.getString("login");
    }

    private enum Operation {
        AUTHORIZE("authorize"),
        ACCESS_TOKEN("access_token");

        private String operation;

        Operation(String operation) {
            this.operation = operation;
        }

    }

    private enum Param {
        client_id("client_id"),
        client_secret("client_secret"),
        redirect_uri("redirect_uri"),
        scope("scope"),
        code("code");

        private String param;

        Param(String param) {
            this.param = param;
        }
    }
}
