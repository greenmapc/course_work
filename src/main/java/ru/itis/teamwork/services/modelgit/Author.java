package ru.itis.teamwork.services.modelgit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
}
