package com.smartfarming;

import com.smartfarming.model.AgroDealer;
import com.smartfarming.model.Role;
import com.smartfarming.repository.AgroDealerRepository;
import com.smartfarming.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SmartFarmingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFarmingApplication.class, args);
    }

    @Bean
    CommandLineRunner initAgroDealers(UserRepository userRepository, AgroDealerRepository agroDealerRepository, PasswordEncoder encoder) {
        return args -> {
            String[][] dealers = {
                {"AgroChemicals Solutions Ltd", "agrochemicals@example.com", "Kisimenti A&F Plaza, Remera, Gasabo, Kigali", "+250 788 308 678"},
                {"INGABO Plant Health Ltd", "ingabo@example.com", "Umuyenzi Plaza, KN 5 Rd, Kigali", "+250 738 907 967 / +250 787 265 587"},
                {"Agriseeds Group Ltd", "agriseeds@example.com", "Kigali, Rwanda", "+250 788 234 568"},
                {"Rwanda Fertilizer Company (RFC)", "rfc@example.com", "Bugesera / Kigali (distribution centers nationwide)", "+250 789 303 811"},
                {"FertiConnect Rwanda", "ferticonnect@example.com", "Kigali Heights, Kigali", "+250 788 123 456"}
            };

            for (String[] dealerData : dealers) {
                if (!userRepository.existsByEmail(dealerData[1])) {
                    AgroDealer dealer = new AgroDealer();
                    dealer.setCompanyName(dealerData[0]);
                    dealer.setEmail(dealerData[1]);
                    dealer.setLocation(dealerData[2]);
                    dealer.setContactNumber(dealerData[3]);
                    dealer.setPassword(encoder.encode("dealer123"));
                    dealer.setRole(Role.AGRO_DEALER);
                    dealer.setVerified(true);
                    agroDealerRepository.save(dealer);
                }
            }
        };
    }
}
