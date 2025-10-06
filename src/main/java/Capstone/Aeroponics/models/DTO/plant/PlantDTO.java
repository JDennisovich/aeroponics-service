package Capstone.Aeroponics.models.DTO.plant;

import Capstone.Aeroponics.models.DTO.user.UserDTO;
import Capstone.Aeroponics.models.entities.Plant;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantDTO {

    @Id
    private long id;
    private String name;
    private BigDecimal min_ph_level;
    private BigDecimal max_ph_level;
    private int min_ppm;
    private int max_ppm;

    public PlantDTO(Plant plant) {
        this.id = plant.getId();
        this.name = plant.getName();
        this.min_ph_level = plant.getMin_ph_level();
        this.max_ph_level = plant.getMax_ph_level();
        this.min_ppm = plant.getMin_ppm();
        this.max_ppm = plant.getMax_ppm();
    }
}