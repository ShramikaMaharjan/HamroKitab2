package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Service.BookService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class BookController {


    @Autowired
    private BookService bookService;

    // Show add book page
    @GetMapping("/addBook")
    public String addBook(HttpSession session) {

        UserTbl user = (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        return "addBook";
    }
    // Save book
    @PostMapping("/addBook")
    public String addBook(
            @ModelAttribute Book book,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session,
            Model model) throws IOException {

        UserTbl user = (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        book.setOwner(user);

        if (!imageFile.isEmpty()) {
            book.setImage(imageFile.getBytes());
            book.setImageType(imageFile.getContentType());
        }

        bookService.saveBook(book);

        return "redirect:/product";
    }

    // Show book details
    @GetMapping("/bookdetail/{id}")
    public String bookDetails(
            @PathVariable("id") int id,
            Model model) {

        Book book = bookService.getBookById(id);

        if (book == null) {
            return "redirect:/product";
        }

        model.addAttribute("book", book);

        return "bookDetails";
    }


    @GetMapping("/myListings")
    public String myListings(
            HttpSession session,
            Model model) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "books",
                bookService.getBooksByOwner(user)
        );

        return "myListings";
    }

    // Show edit book page
    @GetMapping("/editBook/{id}")
    public String editBook(
            @PathVariable int id,
            HttpSession session,
            Model model) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Book book = bookService.getBookById(id);

        if (book == null) {
            return "redirect:/myListings";
        }

        if (book.getOwner().getId() != user.getId()) {
            return "redirect:/myListings";
        }

        model.addAttribute("book", book);

        return "editBook";
    }


    // Update book
    @PostMapping("/editBook")
    public String updateBook(
            @ModelAttribute Book book,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session) throws IOException {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Book existingBook =
                bookService.getBookById(book.getId());

        if (existingBook == null) {
            return "redirect:/myListings";
        }

        if (existingBook.getOwner().getId() != user.getId()) {
            return "redirect:/myListings";
        }

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setCategory(book.getCategory());
        existingBook.setSemester(book.getSemester());
        existingBook.setSubject(book.getSubject());
        existingBook.setInstitution(book.getInstitution());
        existingBook.setCondition(book.getCondition());
        existingBook.setPrice(book.getPrice());
        existingBook.setDescription(book.getDescription());

        if (!imageFile.isEmpty()) {
            existingBook.setImage(imageFile.getBytes());
            existingBook.setImageType(imageFile.getContentType());
        }

        bookService.saveBook(existingBook);

        return "redirect:/myListings";
    }
    @PostMapping("/deleteBook")
    public String deleteBook(
            @RequestParam("bookId") int bookId,
            HttpSession session) {

        UserTbl user = (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Book book = bookService.getBookById(bookId);

        if (book == null) {
            return "redirect:/myListings";
        }

        if (book.getOwner().getId() != user.getId()) {
            return "redirect:/myListings";
        }

        // Check whether the book has messages
        if (bookService.hasMessages(book)) {
            return "redirect:/myListings?error=messages";
        }

        bookService.deleteBook(book);

        return "redirect:/myListings?success=deleted";
    }
}