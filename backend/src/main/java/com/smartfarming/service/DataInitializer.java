package com.smartfarming.service;

import com.smartfarming.model.User;
import com.smartfarming.model.Role;
import com.smartfarming.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "mudahemukafidela90@gmail.com";
        
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setVerified(true);
            userRepository.save(admin);
            System.out.println("System Admin created: " + adminEmail);
        } else {
            // Ensure existing user is admin
            User existing = userRepository.findByEmail(adminEmail).get();
            if (existing.getRole() != Role.ADMIN) {
                existing.setRole(Role.ADMIN);
                existing.setVerified(true);
                userRepository.save(existing);
                System.out.println("System User promoted to Admin: " + adminEmail);
            }
        }
    }
}
