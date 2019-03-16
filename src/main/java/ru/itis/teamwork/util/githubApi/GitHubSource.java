package ru.itis.teamwork.util.githubApi;

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
