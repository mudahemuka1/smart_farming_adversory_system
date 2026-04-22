package com.smartfarming.dto;

import com.smartfarming.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    private Role role;

    // Additional fields for profile
    private String fullName;
    private String location;
    private String contactNumber;
    private String companyName;
    private String specialization;
}
