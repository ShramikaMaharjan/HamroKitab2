package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token,
                              Model model) {

        Optional<UserTbl> optionalUser =
                userRepository.findByVerificationToken(token);

        if (optionalUser.isEmpty()) {
            model.addAttribute("message", "Invalid or expired verification link.");
            return "verification";
        }

        UserTbl user = optionalUser.get();

        user.setVerified(true);
        user.setVerificationToken(null);

        userRepository.save(user);

        model.addAttribute("message",
                "Your email has been verified successfully. You can now login.");

        return "verification";
    }
}