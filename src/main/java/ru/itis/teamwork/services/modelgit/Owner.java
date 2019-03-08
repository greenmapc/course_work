package ru.itis.teamwork.services.modelgit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Owner {

    private String login;
    private String avatarUrl;
    private String url;

}

