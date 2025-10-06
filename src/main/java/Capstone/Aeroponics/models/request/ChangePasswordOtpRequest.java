package Capstone.Aeroponics.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordOtpRequest(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format is invalid")
        String email
) {}

