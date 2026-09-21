package io.virinchi.hamrokitab2.RestAPIController;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Model.Wishlist;
import io.virinchi.hamrokitab2.Repository.BookRepository;
import io.virinchi.hamrokitab2.Repository.WishlistRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistApiController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private BookRepository bookRepository;


    // Get logged-in user's wishlist
    @GetMapping
    public ResponseEntity<?> getWishlist(HttpSession session) {

        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401)
                    .body("Please login first");
        }

        List<Wishlist> wishlist =
                wishlistRepository.findByUser(loggedInUser);

        return ResponseEntity.ok(wishlist);
    }


    // Add book to wishlist
    @PostMapping("/{bookId}")
    public ResponseEntity<?> addToWishlist(
            @PathVariable int bookId,
            HttpSession session) {

        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401)
                    .body("Please login first");
        }

        Book book = bookRepository.findById(bookId)
                .orElse(null);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        boolean alreadyExists =
                wishlistRepository.existsByUserAndBook(
                        loggedInUser,
                        book
                );

        if (alreadyExists) {
            return ResponseEntity.badRequest()
                    .body("Book is already in your wishlist");
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser(loggedInUser);
        wishlist.setBook(book);

        Wishlist savedWishlist =
                wishlistRepository.save(wishlist);

        return ResponseEntity.ok(savedWishlist);
    }


    // Remove book from wishlist
    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable int bookId,
            HttpSession session) {

        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401)
                    .body("Please login first");
        }

        Book book = bookRepository.findById(bookId)
                .orElse(null);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        boolean exists =
                wishlistRepository.existsByUserAndBook(
                        loggedInUser,
                        book
                );

        if (!exists) {
            return ResponseEntity.badRequest()
                    .body("Book is not in your wishlist");
        }

        wishlistRepository.deleteByUserAndBook(
                loggedInUser,
                book
        );

        return ResponseEntity.ok(
                "Book removed from wishlist"
        );
    }
}