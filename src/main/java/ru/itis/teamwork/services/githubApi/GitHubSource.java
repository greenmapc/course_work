package ru.itis.teamwork.services.githubApi;

public enum GitHubSource {

    USER("/user"),
    REPOS("/repos"),
    CONTENTS("/contents"),
    COMMITS("/commits"),
    COMMENTS("/comments");

    protected String source;

    public String getSource(){
        return this.source;
    }

    GitHubSource(String source) {
        this.source = source;
    }

}
