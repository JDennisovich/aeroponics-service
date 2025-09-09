package Capstone.Aeroponics.models.DTO.nutrient;

import Capstone.Aeroponics.models.DTO.tower.TowerDTO;
import Capstone.Aeroponics.models.entities.Nutrient;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutrientDTO {

    @Id
    private long id;
    private TowerDTO tower;
    private LocalTime time;
    private int ph_level;
    private int ppm;

    public NutrientDTO(Nutrient nutrient) {
        this.id = nutrient.getId();
        this.tower = new TowerDTO(nutrient.getTower());
        this.time = nutrient.getTime();
        this.ph_level = nutrient.getPh_level();
        this.ppm = nutrient.getPpm();
    }

    //TODO: ADD CONFIRM PASSWORD TO USER REGISTER
}