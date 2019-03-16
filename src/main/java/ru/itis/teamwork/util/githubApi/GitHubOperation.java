package ru.itis.teamwork.util.githubApi;

public enum GitHubOperation {
    AUTHORIZE("/authorize"),
    ACCESS_TOKEN("/access_token"),
    COMMITS("/commits"),
    EMAILS("/emails");

    protected String operation;

    public String getOperation(){
        return this.operation;
    }

    GitHubOperation(String operation) {
        this.operation = operation;
    }
}
