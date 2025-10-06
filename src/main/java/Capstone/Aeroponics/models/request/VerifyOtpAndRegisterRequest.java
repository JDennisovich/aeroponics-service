package Capstone.Aeroponics.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyOtpAndRegisterRequest(
        @NotBlank(message = "First name is mandatory")
        String firstName,
        
        @NotBlank(message = "Last name is mandatory")
        String lastName,
        
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format is invalid")
        String email,
        
        @NotBlank(message = "Password is mandatory")
        String password,
        
        @NotBlank(message = "Confirm Password is mandatory")
        String confirmPassword,
        
        @NotBlank(message = "OTP code is mandatory")
        @Pattern(regexp = "^\\d{6}$", message = "OTP must be 6 digits")
        String otpCode
) {
    public UserRO toUserRO() {
        return new UserRO(0, firstName, lastName, email, password, confirmPassword);
    }
}
