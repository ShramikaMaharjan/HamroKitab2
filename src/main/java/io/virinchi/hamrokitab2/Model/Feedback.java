package io.virinchi.hamrokitab2.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 1000)
    private String message;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTbl user;
}