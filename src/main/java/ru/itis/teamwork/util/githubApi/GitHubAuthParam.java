package ru.itis.teamwork.util.githubApi;

public enum GitHubAuthParam {

    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    REDIRECT_URI("redirect_uri"),
    SCOPE("scope"),
    CODE("code");

    protected String param;

    public String getParam(){
        return this.param;
    }

    GitHubAuthParam(String param) {
        this.param = param;
    }
}
