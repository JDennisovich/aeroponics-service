package Capstone.Aeroponics.models.DTO.user;

import Capstone.Aeroponics.models.entities.User;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Id
    private long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;

    public UserDTO(User user) {
        this.id = user.getId();
        this.first_name = user.getFirst_name();
        this.last_name = user.getLast_name();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
