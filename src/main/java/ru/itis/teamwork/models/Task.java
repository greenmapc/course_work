package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "task")
@Data
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "beginning")
    private Date beginning;

    @Column(name = "ending")
    private Date ending;

    @ManyToOne
    @JoinColumn(name = "creator")
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "task_performers",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> performers;

    @ManyToOne
    @JoinColumn(name = "project")
    private Project project;

}
