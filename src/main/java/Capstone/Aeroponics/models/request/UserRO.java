package Capstone.Aeroponics.models.request;

import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import Capstone.Aeroponics.models.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRO( 
    int id,
    @NotBlank(message = "Name is mandatory") String name,
    @NotBlank(message = "Email is mandatory") @Email String email,
    String password
) {
    public User toEntity(User user) {
        if (Objects.isNull(user)) {
            user = new User();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setName(name);
        user.setEmail(email);
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        
        return user;
    }
}
