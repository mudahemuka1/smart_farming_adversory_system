package com.smartfarming.controller;

import com.smartfarming.model.*;
import com.smartfarming.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private AgronomistRepository agronomistRepository;

    @Autowired
    private AgroDealerRepository agroDealerRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/farmers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    @GetMapping("/agronomists")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Agronomist> getAllAgronomists() {
        return agronomistRepository.findAll();
    }

    @GetMapping("/dealers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AgroDealer> getAllDealers() {
        return agroDealerRepository.findAll();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody java.util.Map<String, Object> payload) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        if (payload.containsKey("verified")) {
            boolean isVer = Boolean.parseBoolean(payload.get("verified").toString());
            user.setVerified(isVer);
        }
        if (payload.containsKey("role")) {
            user.setRole(Role.valueOf(payload.get("role").toString()));
        }
        
        User updated = userRepository.save(user);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> verifyUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setVerified(true);
        return ResponseEntity.ok(userRepository.save(user));
    }
}
