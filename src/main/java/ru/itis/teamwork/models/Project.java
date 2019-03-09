package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "project")
@Data
@Builder
@EqualsAndHashCode
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "git_link")
    private String gitLink;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "project")
    private Set<ProjectFile> files;

    @OneToMany(mappedBy = "project")
    private Set<Task> projectTasks;

    @ManyToMany
    @JoinTable(
            name = "user_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "team_leader_id")
    private User teamLeader;


}
