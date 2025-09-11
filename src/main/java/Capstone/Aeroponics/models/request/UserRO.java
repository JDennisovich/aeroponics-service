package Capstone.Aeroponics.models.request;

import java.util.Objects;

import Capstone.Aeroponics.models.request.validations.PasswordFormatValidation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import Capstone.Aeroponics.models.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRO(
        long id,
        @NotBlank(message = "First name is mandatory") String first_name,
        @NotBlank(message = "Last name is mandatory") String last_name,
        @NotBlank(message = "Email is mandatory") @Email String email,
        @NotBlank(message = "Password is mandatory") @PasswordFormatValidation String password,
        @NotBlank(message = "Confirm Password is mandatory") String confirmPassword
) {
    public User toEntity(User user) {
        if (Objects.isNull(user)) {
            user = new User();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setEmail(email);

        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        return user;
    }
}
