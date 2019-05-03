package ru.itis.teamwork.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "project_file")
@Data
public class ProjectFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "hash_name", nullable = false)
    private String hashName;

    @Column(name = "extension", nullable = false)
    private String extension;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
