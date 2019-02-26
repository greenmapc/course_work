package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "project_file")
@Data
@Builder
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "hash_name")
    private String hash_name;

    @Column(name = "extension")
    private String extension;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
