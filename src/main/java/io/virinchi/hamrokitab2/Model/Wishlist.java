package io.virinchi.hamrokitab2.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTbl user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}