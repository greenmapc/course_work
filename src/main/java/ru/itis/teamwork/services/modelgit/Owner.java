package ru.itis.teamwork.services.modelgit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {

    @JsonProperty("login")
    private String login;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("url")
    private String url;

}

