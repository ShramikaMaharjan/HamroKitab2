package io.virinchi.hamrokitab2.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Base64;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String category;

    private String semester;

    private String subject;

    private String institution;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private double price;

    @Column(length = 1000)
    private String description;

    // Book image stored in the database
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    private String imageType;

    // Automatically connected to the logged-in user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTbl owner;

    /**
     * Returns the book image encoded as a Base64 string.
     * Useful for rendering images directly in Thymeleaf templates.
     *
     * @return Base64-encoded image string, or null if no image is present.
     */
    public String getImageBase64() {

        if (image == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(image);
    }
}