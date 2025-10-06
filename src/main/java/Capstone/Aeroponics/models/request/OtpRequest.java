package Capstone.Aeroponics.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OtpRequest(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format is invalid")
        String email,
        
        @NotBlank(message = "OTP code is mandatory")
        @Pattern(regexp = "^\\d{6}$", message = "OTP must be 6 digits")
        String otpCode
) {}
