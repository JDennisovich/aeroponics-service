package Capstone.Aeroponics.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyChangePasswordOtpRequest(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format is invalid")
        String email,
        
        @NotBlank(message = "New password is mandatory")
        String newPassword,
        
        @NotBlank(message = "Confirm password is mandatory")
        String confirmPassword,
        
        @NotBlank(message = "OTP code is mandatory")
        @Pattern(regexp = "^\\d{6}$", message = "OTP must be 6 digits")
        String otpCode
) {}

