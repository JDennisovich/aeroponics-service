package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Nutrient_log;
import Capstone.Aeroponics.models.entities.Tower;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record Nutrient_logsRO(
        int id,
        @NotNull(message = "Tower ID is mandatory") Tower tower,
        @NotNull(message = "Time is mandatory") LocalTime time,
        @NotNull(message = "PH level is mandatory") int ph_level,
        @NotNull(message = "PPM is mandatory") int ppm,
        @NotNull(message = "Water Level is mandatory") int water_level
) {
    public Nutrient_log toEntity(Nutrient_log nutrientLog) {
        if (nutrientLog == null) {
            nutrientLog = new Nutrient_log();
        }
        nutrientLog.setTower(tower);
        nutrientLog.setTime(time);
        nutrientLog.setPh_level(ph_level);
        nutrientLog.setPpm(ppm);
        nutrientLog.setPpm(water_level);

        return nutrientLog;
    }
}


