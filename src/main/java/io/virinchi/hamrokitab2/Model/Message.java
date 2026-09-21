package io.virinchi.hamrokitab2.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 1000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserTbl sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserTbl receiver;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


}
