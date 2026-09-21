package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;


    // Show registration page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }


    // Process registration form
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        UserTbl user = new UserTbl();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        String result = userService.registerUser(user);

        if (result.equals("Email already registered")) {
            model.addAttribute("error", result);
            return "register";
        }

        model.addAttribute("success", result);
        return "register";
    }

}