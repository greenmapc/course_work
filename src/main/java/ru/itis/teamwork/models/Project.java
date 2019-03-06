package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "project")
@Data
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "git_link")
    private String gitLink;

    @OneToMany(mappedBy = "project")
    private Set<ProjectFile> files;

    @OneToMany(mappedBy = "project")
    private Set<Task> projectTasks;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "team_leader_id")
    private User teamLeader;


}
