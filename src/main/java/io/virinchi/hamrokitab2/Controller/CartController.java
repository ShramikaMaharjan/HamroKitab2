package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private BookRepository bookRepository;


    // ================= ADD TO CART =================

    @GetMapping("/cart/add/{id}")
    public String addToCart(
            @PathVariable int id,
            HttpSession session) {

        // Check if user is logged in
        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            // Remember the book the user wanted to add
            session.setAttribute("pendingCartBookId", id);

            return "redirect:/login";
        }

        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            return "redirect:/product";
        }

        List<Book> cart =
                (List<Book>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        boolean alreadyAdded = false;

        for (Book cartBook : cart) {

            if (cartBook.getId() == book.getId()) {
                alreadyAdded = true;
                break;
            }
        }

        if (!alreadyAdded) {
            cart.add(book);
        }

        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }


    // ================= SHOW CART =================

    @GetMapping("/cart")
    public String showCart(
            HttpSession session,
            Model model) {

        // Check if user is logged in
        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            // Remember that user wanted to view cart
            session.setAttribute("pendingCart", true);

            return "redirect:/login";
        }

        List<Book> cart =
                (List<Book>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        double total = 0;

        for (Book book : cart) {
            total += book.getPrice();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "cart";
    }


    // ================= REMOVE FROM CART =================

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(
            @PathVariable int id,
            HttpSession session) {

        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Book> cart =
                (List<Book>) session.getAttribute("cart");

        if (cart != null) {

            cart.removeIf(book -> book.getId() == id);

            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }


    // ================= CHECKOUT =================

    @GetMapping("/cart/checkout")
    public String cartCheckout(
            HttpSession session,
            Model model) {

        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            session.setAttribute("pendingCartCheckout", true);

            return "redirect:/login";
        }

        List<Book> cart =
                (List<Book>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        // Take the first book from the cart
        Book book = cart.get(0);

        model.addAttribute("book", book);

        return "order";
    }
}
