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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GitHubApi {

    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String GITHUB_API_AUTH;
    private String REDIRECT;
    private String GITHUB;
    private HttpClient httpClient;

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
        URIBuilder uriBuilder = new URIBuilder(GITHUB.concat("/").concat("emails"));
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
        System.out.println(obj.toString());
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
