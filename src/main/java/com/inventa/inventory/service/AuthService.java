package com.inventa.inventory.service;

import com.inventa.inventory.model.User;
import com.inventa.inventory.repository.UserRepository;
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
