package ru.itis.teamwork.models;

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

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "hash_name", nullable = false, unique = true)
    private String hashName;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "src")
    private String path;

    @OneToOne(mappedBy = "img")
    private User user;

    public String getImg() {
        return path + hashName + type;
    }

    @PrePersist
    void preInsert() {
        if(this.path == null) {
            this.path = "/WEB-INF/userData/mainImg/";
        }

    }
}
