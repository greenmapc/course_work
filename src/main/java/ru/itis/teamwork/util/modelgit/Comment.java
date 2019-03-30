package ru.itis.teamwork.util.modelgit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    @NotNull
    @JsonProperty("body")
    private String body;
    @JsonProperty("path")
    private String path;
    @JsonProperty("position")
    private Integer position;
    @JsonProperty("id")
    private Long commentId;
}
