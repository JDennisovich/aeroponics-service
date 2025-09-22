package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.models.enums.WaterLevel;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public record TowerRO(
    int id,
    @NotNull(message = "User ID is mandatory") User user,
    @NotNull(message = "Plant is mandatory") Plant plant,
    @NotNull(message = "Name is mandatory") String name,
    @NotNull(message = "Time is mandatory") LocalTime time,
    @NotNull(message = "Water level is mandatory") WaterLevel water_level,
    @NotNull(message = "Frequency is mandatory") int frequency,
    @NotNull(message = "Start Date is mandatory") LocalDate start_date,
    @NotNull(message = "End Date is mandatory") LocalDate end_date
) {
    public Tower toEntity(Tower tower) {
        if (tower == null) {
            tower = new Tower();
        }
        tower.setUser(user);
        tower.setPlant(plant);
        tower.setTime(time);
        tower.setName(name);
        tower.setStatus(true);

        // 👇 set WaterLevel, default to MEDIUM if null
        tower.setWaterLevel(Objects.nonNull(water_level) ? water_level : WaterLevel.MEDIUM);

        tower.setFrequency(frequency);
        tower.setStart_date(start_date);
        tower.setEnd_date(end_date);

        return tower;
    }
}