package com.inventa.inventory.service;

import com.inventa.inventory.model.User;
import com.inventa.inventory.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean login(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if(user != null){
            return user.getPassword().equals(password);
        }

        return false;
    }

    public String resetPassword(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            String errorMessage = "User not found.";

            return errorMessage;
        }

        String newPassword = generateRandomPassword(10);

        user.setPassword(newPassword);
        userRepository.save(user);

        return newPassword;
    }

    public String changePassword(String oldPassword, String newPassword, String confirmPassword, HttpSession session) {
        String result = "Password changed successfully.";
        String email = (String) session.getAttribute("user");

        User user = userRepository.findByEmail(email);

        if(!oldPassword.equals(user.getPassword())) {
            result = "Your old password is wrong.";
        } else {
            if(!newPassword.equals(confirmPassword)) {
                result = "Confirm new password and new password must be the same.";
            } else {
                String pattern = "^(?=.*[A-Z])(?=.*\\d).{10,}$";

                if(!newPassword.matches(pattern)) {
                    result = "New password does not match the requirements.";
                } else {
                    user.setPassword(newPassword);
                    userRepository.save(user);
                }
            }
        }

        return result;
    }

    private String generateRandomPassword(int length) {
        String numbers = "0123456789";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder newPassword = new StringBuilder(); // lebih cepat dan hemat memori dibanding string biasa

        for(int i = 0; i < 3; i++) {
            newPassword.append(numbers.charAt(random.nextInt(numbers.length())));
            newPassword.append(lowercaseChars.charAt(random.nextInt(lowercaseChars.length())));
            newPassword.append(uppercaseChars.charAt(random.nextInt(uppercaseChars.length())));
        }

        newPassword.append(lowercaseChars.charAt(random.nextInt(lowercaseChars.length()))); // append 1 huruf terakhir

        return newPassword.toString();
    }
}
