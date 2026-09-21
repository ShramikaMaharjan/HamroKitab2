package io.virinchi.hamrokitab2.Controller;

import io.virinchi.hamrokitab2.Model.Feedback;
import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Open feedback page
    @GetMapping("/feedback")
    public String feedbackPage() {
        return "feedback";
    }

    // Save feedback
    @PostMapping("/feedback")
    public String submitFeedback(
            @RequestParam("message") String message,
            @RequestParam("rating") int rating,
            jakarta.servlet.http.HttpSession session) {

        UserTbl loggedInUser =
                (UserTbl) session.getAttribute("loggedInUser");

        // User must be logged in
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Feedback feedback = new Feedback();

        feedback.setMessage(message);
        feedback.setRating(rating);
        feedback.setUser(loggedInUser);

        feedbackService.saveFeedback(feedback);

        return "redirect:/?feedbackSuccess=true";
    }
}