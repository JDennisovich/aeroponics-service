package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Nutrient;
import Capstone.Aeroponics.models.entities.Tower;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record NutrientRO (
        int id,
        @NotNull(message = "Tower ID is mandatory") Tower tower,
        @NotNull(message = "Time is mandatory") LocalTime time,
        @NotNull(message = "PH level is mandatory") int ph_level,
        @NotNull(message = "PPM is mandatory") int ppm
) {
    public Nutrient toEntity(Nutrient nutrient) {
        if (nutrient == null) {
            nutrient = new Nutrient();
        }
        nutrient.setTower(tower);
        nutrient.setTime(time);
        nutrient.setPh_level(ph_level);
        nutrient.setPpm(ppm);

        return nutrient;
    }
}


