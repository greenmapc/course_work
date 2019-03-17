package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_main_img")
@Data
public class UserMainImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "hash_name")
    private String hashName;

    @Column(name = "type")
    private String type;

    @Column(name = "src")
    private String path;

    @OneToOne(mappedBy = "img")
    private User user;
}
