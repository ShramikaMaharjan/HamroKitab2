package io.virinchi.hamrokitab2.Service;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.BookRepository;
import io.virinchi.hamrokitab2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;


    /* ================= USERS ================= */

    public List<UserTbl> getAllUsers() {
        return userRepository.findAll();
    }


    /* ================= BOOKS ================= */

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    /* ================= DELETE BOOK ================= */

    public void deleteBook(int bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new RuntimeException("Book not found.");
        }

        bookRepository.deleteById(bookId);
    }


    /* ================= USER COUNT ================= */

    public long getUserCount() {
        return userRepository.count();
    }


    /* ================= BOOK COUNT ================= */

    public long getBookCount() {
        return bookRepository.count();
    }
}