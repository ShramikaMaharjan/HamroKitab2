package io.virinchi.hamrokitab2.Service;

import io.virinchi.hamrokitab2.Model.UserTbl;
import io.virinchi.hamrokitab2.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public String registerUser(UserTbl user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered";
        }

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Set default role
        user.setRole("STUDENT");

        // User is not verified initially
        user.setVerified(false);

        // Generate unique verification token
        String token = UUID.randomUUID().toString();

        user.setVerificationToken(token);

        // Save user in database
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(
                user.getEmail(),
                token
        );

        return "Registration successful. Please check your email to verify your account.";
    }
}