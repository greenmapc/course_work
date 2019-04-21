package ru.itis.teamwork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "project")
@Data
@EqualsAndHashCode
@ToString(exclude = {"users", "teamLeader"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "git_link")
    private String gitLink;

    @Column(name = "description", nullable = false, length = 65535)
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
    @JsonBackReference
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "team_leader_id", nullable = false)
    private User teamLeader;


}
