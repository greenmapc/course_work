package ru.itis.teamwork.models;

import lombok.*;

import javax.ejb.Remote;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
//@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="chat")
public class Chat {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    private Set<Message> messages;

    @OneToOne(mappedBy = "chat")
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "user_chats",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;
}
