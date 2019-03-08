package ru.itis.teamwork.services.modelgit;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Commit {

    private String sha;
    private Author author;
    private String htmlUrl;
    private String message;
}
