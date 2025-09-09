package Capstone.Aeroponics.models.DTO.nutrient;

import Capstone.Aeroponics.models.entities.Nutrient;
import Capstone.Aeroponics.models.entities.Tower;
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
public class NutrientPhLevelDTO {

    @Id
    private long id;
    private int phLevel;
    private LocalTime time;

    public NutrientPhLevelDTO(Nutrient nutrient) {
        this.id = nutrient.getId();
        this.phLevel = nutrient.getPh_level();
        this.time = nutrient.getTime();
    }
}
