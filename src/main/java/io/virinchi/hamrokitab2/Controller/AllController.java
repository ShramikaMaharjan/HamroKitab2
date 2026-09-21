package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Repository.UserRepository;
import io.virinchi.hamrokitab2.Service.BookService;
import io.virinchi.hamrokitab2.Service.FeedbackService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AllController {


    @Autowired
    private UserRepository uRepo;

    @Autowired
    private BookService bookService;

    @Autowired
    private FeedbackService feedbackService;


    // =========================
    // LANDING PAGE
    // =========================

    @GetMapping("/")
    public String firstPage(Model m) {

        m.addAttribute(
                "feedbacks",
                feedbackService.getAllFeedback()
        );

        return "landingold";
    }


    // =========================
    // HOME
    // =========================

    @GetMapping("/home")
    public String homeGet(Model m) {

        m.addAttribute(
                "totalUsers",
                uRepo.findAll()
        );

        m.addAttribute(
                "feedbacks",
                feedbackService.getAllFeedback()
        );

        return "landingold";
    }


    // =========================
    // PRODUCT
    // =========================

    @GetMapping("/product")
    public String product(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String condition,
            Model m,
            HttpSession session) {

        m.addAttribute(
                "books",
                bookService.searchAndFilter(
                        title,
                        category,
                        semester,
                        condition
                )
        );

        // Get wishlist message from session
        String wishlistMessage =
                (String) session.getAttribute("wishlistMessage");

        if (wishlistMessage != null) {

            m.addAttribute(
                    "wishlistMessage",
                    wishlistMessage
            );

            // Remove it after displaying once
            session.removeAttribute("wishlistMessage");
        }

        return "product";
    }


    // =========================
    // CATEGORY
    // =========================

    @GetMapping("/category")
    public String category(

            @RequestParam(required = false) String title,

            @RequestParam(required = false) String category,

            @RequestParam(required = false) String semester,

            @RequestParam(required = false) String condition,

            Model m) {

        m.addAttribute(
                "books",
                bookService.searchAndFilter(
                        title,
                        category,
                        semester,
                        condition
                )
        );

        m.addAttribute("selectedCategory", category);
        m.addAttribute("selectedCondition", condition);
        m.addAttribute("searchTitle", title);

        return "category";
    }

    // =========================
    // ABOUT
    // =========================

    @GetMapping("/about")
    public String about() {
        return "about";
    }


    // =========================
    // SELLER
    // =========================

    @GetMapping("/seller")
    public String seller() {
        return "seller";
    }


    // =========================
    // PROFILE
    // =========================

    @GetMapping("/profile")
    public String profile(
            HttpSession session,
            Model m) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        m.addAttribute(
                "user",
                session.getAttribute("loggedInUser")
        );

        return "profile";
    }

}