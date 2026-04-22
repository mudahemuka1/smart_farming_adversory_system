package com.smartfarming.controller;

import com.smartfarming.dto.JwtResponse;
import com.smartfarming.dto.LoginRequest;
import com.smartfarming.dto.MessageResponse;
import com.smartfarming.dto.SignupRequest;
import com.smartfarming.model.*;
import com.smartfarming.repository.*;
import com.smartfarming.security.JwtUtils;
import jakarta.validation.Valid;
import com.smartfarming.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Random;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FarmerRepository farmerRepository;

    @Autowired
    AgronomistRepository agronomistRepository;

    @Autowired
    AgroDealerRepository agroDealerRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            User userDetails = (User) authentication.getPrincipal();
            
            // Record login time
            userDetails.setLastLoginAt(LocalDateTime.now());
            userRepository.save(userDetails);

            String role = userDetails.getRole().name();
            String name = "";
            if (userDetails instanceof Farmer) {
                name = ((Farmer) userDetails).getFullName();
            } else if (userDetails instanceof Agronomist) {
                name = ((Agronomist) userDetails).getFullName();
            } else if (userDetails instanceof AgroDealer) {
                name = ((AgroDealer) userDetails).getCompanyName();
            } else {
                name = userDetails.getEmail();
            }

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getEmail(),
                    role,
                    name));
        } catch (DisabledException e) {
            return ResponseEntity
                    .status(403)
                    .body(new MessageResponse("Error: Your account is not verified. Please check your email."));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Error: Unauthorized. Check credentials."));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            logger.info("Sign up attempt for email: {}", signUpRequest.getEmail());
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already taken!"));
            }

            // Create new user's account
            User user;
            Role role = signUpRequest.getRole();

            switch (role) {
                case FARMER:
                    Farmer farmer = new Farmer();
                    farmer.setEmail(signUpRequest.getEmail());
                    farmer.setFullName(signUpRequest.getFullName());
                    farmer.setLocation(signUpRequest.getLocation());
                    farmer.setContactNumber(signUpRequest.getContactNumber());
                    user = farmer;
                    break;
                case AGRONOMIST:
                    Agronomist agronomist = new Agronomist();
                    agronomist.setEmail(signUpRequest.getEmail());
                    agronomist.setFullName(signUpRequest.getFullName());
                    agronomist.setSpecialization(signUpRequest.getSpecialization());
                    agronomist.setContactNumber(signUpRequest.getContactNumber());
                    user = agronomist;
                    break;
                case AGRO_DEALER:
                    AgroDealer agroDealer = new AgroDealer();
                    agroDealer.setEmail(signUpRequest.getEmail());
                    agroDealer.setCompanyName(signUpRequest.getCompanyName());
                    agroDealer.setLocation(signUpRequest.getLocation());
                    agroDealer.setContactNumber(signUpRequest.getContactNumber());
                    user = agroDealer;
                    break;
                case ADMIN:
                default:
                    user = new User();
                    user.setEmail(signUpRequest.getEmail());
                    break;
            }

            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            user.setRole(role);
            user.setVerified(false);

            // Generate Verification Code
            String code = String.format("%06d", new Random().nextInt(999999));
            user.setVerificationCode(code);
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(3));

            userRepository.save(user);

            // Send Email
            emailService.sendVerificationEmail(user.getEmail(), code);

            logger.info("User created and verification email sent to: {}", signUpRequest.getEmail());
            return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email for the verification code."));
        } catch (Exception e) {
            logger.error("Signup error for email {}: {}", signUpRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new MessageResponse("Signup failed: " + e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String email, @RequestParam String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        if (user.isVerified()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User already verified."));
        }

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            // Re-generate if expired
            String newCode = String.format("%06d", new Random().nextInt(999999));
            user.setVerificationCode(newCode);
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(3));
            userRepository.save(user);

            emailService.sendVerificationEmail(user.getEmail(), newCode);
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Code expired. A new code has been sent to your email."));
        }

        if (user.getVerificationCode().equals(code)) {
            user.setVerified(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Account verified successfully! You can now login."));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid verification code."));
        }
    }

    @PostMapping("/resend-code")
    public ResponseEntity<?> resendCode(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        if (user.isVerified()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User already verified."));
        }

        String newCode = String.format("%06d", new Random().nextInt(999999));
        user.setVerificationCode(newCode);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(3));
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), newCode);
        return ResponseEntity.ok(new MessageResponse("New verification code sent to your email."));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream()
            .filter(user -> user.getRole() != Role.AGRO_DEALER)
            .map(user -> {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", user.getId());
            map.put("email", user.getEmail());
            map.put("role", user.getRole().name());
            map.put("createdAt", user.getCreatedAt());
            map.put("lastLoginAt", user.getLastLoginAt());
            map.put("isVerified", user.isVerified());
            
            String name = "";
            if (user instanceof Farmer) {
                name = ((Farmer) user).getFullName();
            } else if (user instanceof Agronomist) {
                name = ((Agronomist) user).getFullName();
            } else if (user instanceof AgroDealer) {
                name = ((AgroDealer) user).getCompanyName();
            } else {
                name = "Admin / System";
            }
            map.put("name", name);
            
            return map;
        }).toList());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id) || id == 1) { // Prevent deleting primary admin
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found or protected."));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully."));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody java.util.Map<String, String> updates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        
        if (id == 1 && updates.containsKey("role")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Admin role cannot be changed."));
        }

        if (updates.containsKey("isVerified")) {
            user.setVerified(Boolean.parseBoolean(updates.get("isVerified")));
        }
        
        if (updates.containsKey("role")) {
            user.setRole(Role.valueOf(updates.get("role")));
        }

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User updated successfully."));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getSystemStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalFarmers", farmerRepository.count());
        stats.put("totalAgronomists", agronomistRepository.count());
        stats.put("totalAgroDealers", agroDealerRepository.count());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/agro-dealers")
    public ResponseEntity<?> getAgroDealers() {
        return ResponseEntity.ok(agroDealerRepository.findAll().stream().map(dealer -> {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", dealer.getId());
            map.put("companyName", dealer.getCompanyName());
            map.put("email", dealer.getEmail());
            map.put("location", dealer.getLocation());
            map.put("contactNumber", dealer.getContactNumber());
            map.put("fullTexts", dealer.getFullTexts());
            return map;
        }).toList());
    }
}
