package ru.itis.teamwork.services.modelgit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryGithub {

    private String name;
    private Boolean isPrivate;
    private Owner owner;
    private String description;
    private String htmlUrl;
    private String language;
    private Long id;


}
