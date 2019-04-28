package ru.itis.teamwork.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Builder
@Getter
//@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="message")
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "text", length = 1024)
    private String text;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private User sender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private Date date;

    private Float timestamp;

    public Message (WebSocketMessage webSocketMessage){
        this.text = webSocketMessage.getText();
        this.sender = webSocketMessage.getUser();
        this.date = new Date();
    }
}
