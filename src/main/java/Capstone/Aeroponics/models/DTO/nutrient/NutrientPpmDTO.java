package Capstone.Aeroponics.models.DTO.nutrient;

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
public class NutrientPpmDTO {

    @Id
    private long id;
    private int ppm;
    private LocalTime time;

    public NutrientPpmDTO(Nutrient nutrient) {
        this.id = nutrient.getId();
        this.ppm = nutrient.getPpm();
        this.time = nutrient.getTime();
    }
}
