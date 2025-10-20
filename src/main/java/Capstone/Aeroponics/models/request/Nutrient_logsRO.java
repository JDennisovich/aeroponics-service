package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Nutrient_log;
import Capstone.Aeroponics.models.entities.Tower;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Nutrient_logsRO(
        int id,
        @NotNull(message = "Tower is mandatory") TowerRef tower,
        @NotNull(message = "Time is mandatory")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime time,
        @NotNull(message = "PH level is mandatory") BigDecimal ph_level,
        @NotNull(message = "PPM is mandatory") BigDecimal ppm,
        @NotNull(message = "Water Level is mandatory") Integer water_level,
        BigDecimal water_temperature
) {
    public record TowerRef(Long id) {}
    public Nutrient_log toEntity(Nutrient_log nutrientLog) {
        if (nutrientLog == null) {
            nutrientLog = new Nutrient_log();
        }
        // Build Tower entity from just the ID
        Tower towerEntity = new Tower();
        towerEntity.setId(tower.id());
        nutrientLog.setTower(towerEntity);
        // Set the full datetime directly
        nutrientLog.setTime(time);
        nutrientLog.setPh_level(ph_level);
        nutrientLog.setPpm(ppm);
        nutrientLog.setWater_level(water_level);
        nutrientLog.setWater_temperature(water_temperature);

        return nutrientLog;
    }
}


