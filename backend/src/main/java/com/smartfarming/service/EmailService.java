package com.smartfarming.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("mudahemukafidela90@gmail.com", "SmartFarming System");
            helper.setTo(to);
            helper.setSubject("Verify Your SmartFarming Account");

            String content = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<h2 style='color: #2e7d32;'>Welcome to SmartFarming!</h2>" +
                    "<p>Your registration is almost complete. Please use the verification code below to activate your account:</p>" +
                    "<div style='font-size: 24px; font-weight: bold; color: #2e7d32; padding: 10px; background: #f1f8e9; display: inline-block; border-radius: 5px;'>" +
                    code + "</div>" +
                    "<p style='color: #666; margin-top: 20px;'>This code will expire in 3 minutes.</p>" +
                    "<p>If you did not request this, please ignore this email.</p>" +
                    "</div>";

            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
