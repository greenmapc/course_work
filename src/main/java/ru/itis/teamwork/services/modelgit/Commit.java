package ru.itis.teamwork.services.modelgit;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {


    @JsonProperty("sha")
    private String sha;
    @JsonProperty("$.commit.author")
    private Author author;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonPath("$.commit.message")
    private String message;
}
