package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.entities.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TowerRO(
    int id,
    @NotNull(message = "User ID is mandatory") User user,
    @NotNull(message = "Plant is mandatory") Plant plant,
    @NotNull(message = "Time is mandatory") LocalTime time,
    @NotNull(message = "Water level is mandatory") int water_level,
    @NotNull(message = "Frequency is mandatory") int frequency,
    @NotNull(message = "Start Date is mandatory") int start_date,
    @NotNull(message = "End Date is mandatory") int end_date
) {
    public Tower toEntity(Tower tower) {
        if (tower == null) {
            tower = new Tower();
        }
        tower.setUser(user);
        tower.setPlant(plant);
        tower.setTime(time);
        tower.setWaterLevel(water_level);
        tower.setFrequency(frequency);
        tower.setStart_date(start_date);
        tower.setEnd_date(end_date);

        return tower;
    }
}