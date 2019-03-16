package ru.itis.teamwork.util.modelgit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryGithubModel {

    @NotNull
    @JsonProperty("name")
    private String name;

    /** A URL with more information about the repository. */
    @JsonProperty("home_page")
    private String homepage;

    /** Either true to create a private repository or false to create a public one.
     * Creating private repositories requires a paid GitHub account. Default: false. */
    @JsonProperty("private")
    private Boolean isPrivate;

    /** Either true to enable issues for this repository or false to disable them. Default: true. */
    @Builder.Default
    @JsonProperty("has_issues")
    private Boolean hasIssues = true;

    /** Either true to enable projects for this repository or false to disable them. Default: true.
     *  Note: If you're creating a repository in an organization that has disabled repository projects,
     *  the default is false, and if you pass true, the API returns an error */
    @Builder.Default
    @JsonProperty("has_projects")
    private Boolean hasProjects = true;

    /** Either true to enable the wiki for this repository or false to disable it. Default: true */
    @Builder.Default
    @JsonProperty("has_wiki")
    private Boolean hasWiki = true;

    /** The id of the team that will be granted access to this repository.
     * This is only valid when creating a repository in an organization.*/
    @JsonProperty("team_id")
    private Integer teamId;

    /** Pass true to create an initial commit with empty README. Default: false.*/
    @JsonProperty("auto_init")
    private Boolean autoInit;

    /** Desired language or platform .gitignore template to apply.
     * Use the name of the template without the extension.
     * For example, "Haskell".*/
    @JsonProperty("gitignore_template")
    private String gitignoreTemplate;

    /** Either true to allow squash-merging pull requests, or false to prevent squash-merging. Default: true */
    @Builder.Default
    @JsonProperty("allow_squash_merge")
    private Boolean allowSquashMerge = true;

    /** Either true to allow merging pull requests with a merge commit,
     * or false to prevent merging pull requests with merge commits. Default: true */
    @Builder.Default
    @JsonProperty("allow_merge_commit")
    private Boolean allowMergeCommit = true;

    /** Either true to allow rebase-merging pull requests, or false to prevent rebase-merging. Default: true */
    @Builder.Default
    @JsonProperty("allow_rebase_merge")
    private Boolean allowRebaseMerge = true;

    @JsonPath("$.owner")
    private Owner owner;
    @JsonProperty("description")
    private String description;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("language")
    private String language;
    @JsonProperty("id")
    private Long id;

    private List<RepositoryContentModel> repositoryContentModelList;
    private List<Owner> collaborators;

}
