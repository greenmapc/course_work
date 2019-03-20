package ru.itis.teamwork.models;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "hash_name", nullable = false)
    private String hashName;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "src")
//    ToDO: здесь укажем, где по умолчанию будут лежать фотки
//    @ColumnDefault()
    private String path;

    @OneToOne(mappedBy = "img")
    private User user;
}
