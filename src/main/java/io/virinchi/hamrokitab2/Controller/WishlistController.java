package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Service.BookService;
import io.virinchi.hamrokitab2.Service.WishlistService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private BookService bookService;

    // Show wishlist page
    @GetMapping("/wishlist")
    public String wishlistPage(HttpSession session, Model model) {

        UserTbl user = (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("wishlist", wishlistService.getUserWishlist(user));

        String wishlistMessage = (String) session.getAttribute("wishlistMessage");

        if (wishlistMessage != null) {
            model.addAttribute("wishlistMessage", wishlistMessage);
            session.removeAttribute("wishlistMessage");
        }

        return "wishlist";
    }

    // Add book to wishlist
    @PostMapping("/wishlist/add")
    public String addWishlist(@RequestParam("bookId") int bookId, HttpSession session) {

        UserTbl user = (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Book book = bookService.getBookById(bookId);

        if (book == null) {
            return "redirect:/product";
        }

        boolean added = wishlistService.addToWishlist(user, book);

        if (added) {
            session.setAttribute("wishlistMessage", "Book added to your wishlist!");
        } else {
            session.setAttribute("wishlistMessage", "This book is already in your wishlist.");
        }

        return "redirect:/product";
    }

    // Remove book from wishlist
    @PostMapping("/wishlist/remove")
    public String removeWishlist(@RequestParam("bookId") int bookId, HttpSession session) {

        UserTbl user = (UserTbl) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Book book = bookService.getBookById(bookId);

        if (book != null) {
            wishlistService.removeFromWishlist(user, book);

            session.setAttribute(
                    "wishlistMessage",
                    "Book has been removed from your wishlist."
            );
        }

        return "redirect:/wishlist";
    }
}