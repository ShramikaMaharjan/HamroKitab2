package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.Book;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.BookRepository;

import io.virinchi.hamrokitab2.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupLogin {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BookRepository bookRepository;


    // ================= LOGIN PAGE =================

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }


    // ================= LOGIN USER =================

    @PostMapping("/loginUser")
    public String loginUser(

            @RequestParam String email,
            @RequestParam String password,

            HttpSession session,
            Model model) {


        // Find user by email
        UserTbl user =
                userRepository.findByEmail(email).orElse(null);


        // Check email and password
        if (user != null &&
                passwordEncoder.matches(
                        password,
                        user.getPassword()
                )) {


            // ================= EMAIL VERIFICATION =================

            if (!user.isVerified()) {

                model.addAttribute(
                        "error",
                        "Please verify your email before logging in."
                );

                return "login";
            }


            // ================= SAVE USER IN SESSION =================

            session.setAttribute("loggedInUser", user);


            // ================= ADMIN CHECK =================

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {

                return "redirect:/admin";
            }


            // ================= PENDING MESSAGE CHECK =================

            Integer pendingMessageBookId =
                    (Integer) session.getAttribute(
                            "pendingMessageBookId"
                    );


            if (pendingMessageBookId != null) {

                return "redirect:/continueContactSeller";
            }


            // ================= PENDING ORDER CHECK =================

            Integer pendingBookId =
                    (Integer) session.getAttribute(
                            "pendingBookId"
                    );


            if (pendingBookId != null) {

                return "redirect:/continueOrder";
            }


            // ================= PENDING CART ADD =================

            Integer pendingCartBookId =
                    (Integer) session.getAttribute(
                            "pendingCartBookId"
                    );


            if (pendingCartBookId != null) {

                Book book =
                        bookRepository.findById(
                                pendingCartBookId
                        ).orElse(null);


                if (book != null) {

                    java.util.List<Book> cart =
                            (java.util.List<Book>)
                                    session.getAttribute("cart");


                    if (cart == null) {
                        cart = new java.util.ArrayList<>();
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
                }


                // Remove pending action
                session.removeAttribute("pendingCartBookId");

                return "redirect:/cart";
            }


            // ================= PENDING CART =================

            Boolean pendingCart =
                    (Boolean) session.getAttribute(
                            "pendingCart"
                    );


            if (Boolean.TRUE.equals(pendingCart)) {

                session.removeAttribute("pendingCart");

                return "redirect:/cart";
            }


            // ================= PENDING CART CHECKOUT =================

            Boolean pendingCartCheckout =
                    (Boolean) session.getAttribute(
                            "pendingCartCheckout"
                    );


            if (Boolean.TRUE.equals(pendingCartCheckout)) {

                session.removeAttribute("pendingCartCheckout");

                return "redirect:/cart/checkout";
            }


            // ================= NORMAL USER =================

            return "redirect:/";
        }


        // ================= INVALID LOGIN =================

        model.addAttribute(
                "error",
                "Invalid email or password"
        );

        return "login";
    }


    // ================= LOGOUT =================

    @GetMapping("/logoutUser")
    public String logoutUser(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
