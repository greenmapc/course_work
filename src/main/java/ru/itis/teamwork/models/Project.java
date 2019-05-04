package ru.itis.teamwork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "project")
@Data
//@EqualsAndHashCode(exclude = "chat")
@ToString(exclude = {"users", "teamLeader"})
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "git_link")
    private String gitLink;

    @Column(name = "git_repo_name")
    private String gitRepositoryName;

    @Column(name = "description", nullable = false, length = 65535)
    private String description;

    @OneToMany(mappedBy = "project")
    private Set<ProjectFile> files = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<Task> projectTasks = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_chat")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "team_leader_id", nullable = false)
    private User teamLeader;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id) &&
                name.equals(project.name) &&
                Objects.equals(gitLink, project.gitLink) &&
                Objects.equals(description, project.description) &&
                Objects.equals(files, project.files) &&
                Objects.equals(projectTasks, project.projectTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gitLink, description, files, projectTasks);
    }
}
