package io.virinchi.hamrokitab2.RestAPIController;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookApiController {


    @Autowired
    private BookService bookService;

    // GET all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // GET one book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {

        Book book = bookService.getBookById(id);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(book);
    }

    // POST - add a new book
    @PostMapping
    public ResponseEntity<?> addBook(
            @RequestBody Book book,
            HttpSession session) {

        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        // Check if user is logged in
        if (loggedInUser == null) {
            return ResponseEntity.status(401)
                    .body("Please login first");
        }

        // Set the logged-in user as the owner
        book.setOwner(loggedInUser);

        Book savedBook = bookService.saveBook(book);

        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/search")
    public List<Book> searchAndFilter(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String condition) {


        return bookService.searchAndFilter(
                title,
                category,
                semester,
                condition
        );


    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(
            @PathVariable int id,
            HttpSession session) {

        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401)
                    .body("Please login first");
        }

        Book book = bookService.getBookById(id);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        if (book.getOwner().getId() != loggedInUser.getId()) {
            return ResponseEntity.status(403)
                    .body("You can only delete your own book");
        }

        bookService.deleteBook(book);

        return ResponseEntity.ok("Book deleted successfully");
    }

}
