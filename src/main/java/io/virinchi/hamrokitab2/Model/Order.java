package io.virinchi.hamrokitab2.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    // ================= BOOK =================

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


    // ================= BUYER =================

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserTbl buyer;


    // ================= SELLER =================

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserTbl seller;


    // ================= PRICE =================

    @Column(nullable = false)
    private double price;


    // ================= CONTACT DETAILS =================

    @Column(nullable = false)
    private String buyerName;

    @Column(nullable = false)
    private String phoneNumber;


    // ================= DELIVERY DETAILS =================

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 1000)
    private String deliveryAddress;

    @Column(length = 1000)
    private String deliveryNote;


    // ================= ORDER DETAILS =================

    private LocalDateTime orderDate;

    private String status = "PENDING";
}