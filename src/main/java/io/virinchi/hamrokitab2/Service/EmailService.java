package io.virinchi.hamrokitab2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(
            String email,
            String token) {

        String verificationLink =
                "http://localhost:8080/verify?token=" + token;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Verify Your HamroKitab Account"
        );

        message.setText(
                "Welcome to HamroKitab!\n\n" +
                        "Please click the link below to verify your email:\n\n" +
                        verificationLink +
                        "\n\nThank you!"
        );

        mailSender.send(message);
    }
}