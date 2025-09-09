package Capstone.Aeroponics.models.DTO.plant;

import Capstone.Aeroponics.models.DTO.user.UserDTO;
import Capstone.Aeroponics.models.entities.Plant;
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
public class PlantDTO {

    @Id
    private long id;
    private String name;
    private int ph_level;
    private int ppm;
    private UserDTO user;

    public PlantDTO(Plant plant) {
        this.id = plant.getId();
        this.name = plant.getName();
        this.ph_level = plant.getPh_level();
        this.ppm = plant.getPpm();
        this.user = new UserDTO(plant.getUser());
    }
}