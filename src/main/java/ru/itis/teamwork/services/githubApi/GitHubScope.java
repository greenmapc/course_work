package ru.itis.teamwork.services.githubApi;

public enum GitHubScope {
    REPO("repo"),
    GIST("gist"),
    USER("user"),
    DELETE_REPO("delete_repo"),
    WRITE("write"),
    ADMIN_ORG("admin:org"),
    ADMIN_PUBLIC("admin:public_key"),
    ADMIN_ORG_HOOK("admin:org_hook"),
    NOTIFICATIONS("notifications"),
    WRITE_DISCUSSION("write:discussion"),
    ADMIN_GRG_KEY("admin:gpg_key");

    private String scope;

    public String getScope(){
        return this.scope;
    }

    GitHubScope(String scope){this.scope=scope;}
}

