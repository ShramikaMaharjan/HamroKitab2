package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    /* ================= ADMIN CHECK ================= */

    private boolean isAdmin(HttpSession session) {

        UserTbl user =
                (UserTbl) session.getAttribute("loggedInUser");

        return user != null
                && "ADMIN".equalsIgnoreCase(user.getRole());
    }


    /* ================= DASHBOARD ================= */

    @GetMapping("")
    public String dashboard(
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "userCount",
                adminService.getUserCount()
        );

        model.addAttribute(
                "bookCount",
                adminService.getBookCount()
        );

        return "admin-dashboard";
    }


    /* ================= USERS ================= */

    @GetMapping("/users")
    public String users(
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "users",
                adminService.getAllUsers()
        );

        return "admin-users";
    }


    /* ================= BOOKS ================= */

    @GetMapping("/books")
    public String books(
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "books",
                adminService.getAllBooks()
        );

        return "admin-books";
    }


    /* ================= DELETE BOOK ================= */

    @PostMapping("/books/delete/{id}")
    public String deleteBook(
            @PathVariable int id,
            HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {

            adminService.deleteBook(id);

        } catch (Exception e) {

            // If the book has related messages/orders,
            // it may not be possible to delete it.

        }

        return "redirect:/admin/books";
    }
}