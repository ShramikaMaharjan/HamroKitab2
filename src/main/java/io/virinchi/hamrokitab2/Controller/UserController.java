package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserRepository uRepo;

// Autowire is also called dependency injection
// It helps us use the functions of the UserRepository interface

    @PostMapping("/deleteUser")
    public String deletePost(@RequestParam("id") int id, Model m) {

        // Delete user using their ID
        uRepo.deleteById(id);

        // Show updated user list
        m.addAttribute("totalUsers", uRepo.findAll());

        return "home";
    }

    @PostMapping("/editUser")
    public String editUser(@RequestParam("id") int id, Model m) {

        // Find user using their ID
        UserTbl user = uRepo.findById(id).orElse(null);

        // Send user information to editPage.html
        m.addAttribute("user", user);

        return "editPage";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute UserTbl user, Model m) {

        // @ModelAttribute collects form data
        // and matches it with the UserTbl fields
        // save() updates the existing user using the ID

        uRepo.save(user);

        // Show updated user list
        m.addAttribute("totalUsers", uRepo.findAll());

        return "home";
    }

}