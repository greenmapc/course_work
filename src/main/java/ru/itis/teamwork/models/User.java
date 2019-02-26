package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "site_user")
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "about")
    private String about;

    @Column(name = "git_name")
    private String gitName;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "login_id", referencedColumnName = "id")
    private Login login;

    @OneToOne
    @JoinColumn(name = "main_img", referencedColumnName = "id")
    private UserMainImg img;

    @OneToMany(mappedBy = "creator")
    private Set<Task> createdTasks;

    @ManyToMany(mappedBy = "performers")
    private Set<Task> tasks;

    @OneToMany(mappedBy = "user")
    private Set<ProjectCommand> commands;

}
